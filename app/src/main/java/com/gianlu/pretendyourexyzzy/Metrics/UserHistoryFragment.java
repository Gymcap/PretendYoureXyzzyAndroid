package com.gianlu.pretendyourexyzzy.Metrics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gianlu.commonutils.Dialogs.FragmentWithDialog;
import com.gianlu.commonutils.Logging;
import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.pretendyourexyzzy.NetIO.LevelMismatchException;
import com.gianlu.pretendyourexyzzy.NetIO.Models.Metrics.UserHistory;
import com.gianlu.pretendyourexyzzy.NetIO.Pyx;
import com.gianlu.pretendyourexyzzy.NetIO.RegisteredPyx;
import com.gianlu.pretendyourexyzzy.R;

public class UserHistoryFragment extends FragmentWithDialog implements Pyx.OnResult<UserHistory>, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerViewLayout layout;
    private RegisteredPyx pyx;

    @NonNull
    public static UserHistoryFragment get() {
        return new UserHistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = new RecyclerViewLayout(inflater);
        layout.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        layout.getList().addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        layout.enableSwipeRefresh(this, R.color.colorAccent);
        layout.startLoading();

        try {
            pyx = RegisteredPyx.get();
        } catch (LevelMismatchException ex) {
            Logging.log(ex);
            layout.showMessage(R.string.failedLoading, true);
            return layout;
        }

        pyx.getUserHistory(this);

        return layout;
    }

    @Override
    public void onDone(@NonNull UserHistory result) {
        layout.loadListData(new UserHistoryAdapter(getContext(), pyx, result, (UserHistoryAdapter.Listener) getContext()));
    }

    @Override
    public void onException(@NonNull Exception ex) {
        Logging.log(ex);
        layout.showMessage(getString(R.string.failedLoading_reason, ex.getMessage()), true);
    }

    @Override
    public void onRefresh() {
        // TODO
    }
}