package com.gianlu.pretendyourexyzzy.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianlu.commonutils.SuperTextView;
import com.gianlu.pretendyourexyzzy.NetIO.Models.GameInfo;
import com.gianlu.pretendyourexyzzy.R;
import com.gianlu.pretendyourexyzzy.Utils;

import java.util.ArrayList;
import java.util.List;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {
    private final List<GameInfo.Player> players;
    private final Listener listener;
    private final LayoutInflater inflater;

    public PlayersAdapter(Context context, List<GameInfo.Player> players, Listener listener) {
        this.inflater = LayoutInflater.from(context);
        this.players = new ArrayList<>(players);
        this.listener = listener;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return players.get(position).name.hashCode();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GameInfo.Player player = players.get(position);
        holder.name.setText(player.name);
        holder.score.setHtml(R.string.score, player.score);

        switch (player.status) {
            case HOST:
                holder.status.setImageResource(R.drawable.ic_person_black_48dp);
                break;
            case IDLE:
                holder.status.setImageResource(R.drawable.ic_access_time_black_48dp);
                break;
            case JUDGING:
            case JUDGE:
                holder.status.setImageResource(R.drawable.ic_gavel_black_48dp);
                break;
            case PLAYING:
                holder.status.setImageResource(R.drawable.ic_hourglass_empty_black_48dp);
                break;
            case WINNER:
                holder.status.setImageResource(R.drawable.ic_star_black_48dp);
                break;
            case SPECTATOR:
                holder.status.setImageResource(R.drawable.ic_remove_red_eye_black_48dp);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onPlayerSelected(player);
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void playerChanged(@NonNull GameInfo.Player player) {
        int pos = Utils.indexOf(players, player.name);
        if (pos != -1) {
            players.set(pos, player);
            notifyItemChanged(pos);
        }
    }

    public void removePlayer(String nick) {
        int pos = Utils.indexOf(players, nick);
        if (pos != -1) {
            players.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public void newPlayer(GameInfo.Player player) {
        players.add(player);
        notifyItemInserted(players.size() - 1);
    }

    public interface Listener {
        void onPlayerSelected(@NonNull GameInfo.Player player);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final SuperTextView score;
        final ImageView status;

        ViewHolder(ViewGroup parent) {
            super(inflater.inflate(R.layout.item_player, parent, false));
            setIsRecyclable(true);

            name = itemView.findViewById(R.id.playerItem_name);
            score = itemView.findViewById(R.id.playerItem_score);
            status = itemView.findViewById(R.id.playerItem_status);
        }
    }
}
