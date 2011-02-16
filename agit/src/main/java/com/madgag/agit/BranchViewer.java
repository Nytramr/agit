package com.madgag.agit;

import static android.R.id.list;
import static com.google.common.collect.Lists.newArrayList;
import static com.madgag.agit.CommitViewerActivity.commitViewerIntentCreatorFor;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.markupartist.android.widget.ActionBar;

public class BranchViewer extends RepositoryActivity {
    
    public static Intent branchViewerIntentFor(File gitdir, Ref branch) {
		return new GitIntentBuilder("git.branch.VIEW").gitdir(gitdir).branch(branch).toIntent();
	}

	private static final String TAG = "BranchViewer";
	@Override String TAG() { return TAG; }
	
	@InjectView(R.id.actionbar)
	ActionBar actionBar;
	
	@InjectView(list)
	private RevCommitListView revCommitListView;
	
	@Inject
	Repository repository;
	
	@Inject @Named("branch")
	Ref branch;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.branch_view);
		
		actionBar.setTitle(branch.getName());
		revCommitListView.setCommits(commitViewerIntentCreatorFor(repository.getDirectory(), branch), commitListForRepo());
	}

	private List<RevCommit> commitListForRepo() {
		Git git = new Git(repository);
		try {
			Iterable<RevCommit> logWaa = git.log().add(branch.getObjectId()).call();
			List<RevCommit> sampleRevCommits = newArrayList(logWaa);
			
			Log.d(TAG, "Found "+sampleRevCommits.size()+" commits");

			return sampleRevCommits;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
