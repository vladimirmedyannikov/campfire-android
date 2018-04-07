package com.pandulapeter.campfire.feature.home.shared

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.transition.Transition
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.pandulapeter.campfire.R
import com.pandulapeter.campfire.databinding.FragmentSongListBinding
import com.pandulapeter.campfire.feature.shared.TopLevelFragment
import com.pandulapeter.campfire.old.feature.home.shared.SpacesItemDecoration
import com.pandulapeter.campfire.old.util.onEventTriggered
import com.pandulapeter.campfire.util.color
import com.pandulapeter.campfire.util.dimension
import com.pandulapeter.campfire.util.hideKeyboard
import com.pandulapeter.campfire.util.onPropertyChanged

abstract class SongListFragment<out VM : SongListViewModel> : TopLevelFragment<FragmentSongListBinding, VM>(R.layout.fragment_song_list), Transition.TransitionListener {

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        viewModel.adapter.run {
            itemClickListener = { position, clickedView -> mainActivity.openDetailScreen(clickedView, listOf(items[position].song)) }
            downloadActionClickListener = { position -> viewModel.adapter.items[position].let { viewModel.downloadSong(it.song) } }
        }
        viewModel.shouldShowUpdateErrorSnackbar.onEventTriggered {
            showSnackbar(
                message = R.string.library_update_error,
                action = { viewModel.updateData() })
        }
        viewModel.downloadSongError.onEventTriggered { song ->
            song?.let {
                showSnackbar(
                    message = mainActivity.getString(R.string.library_song_download_error, song.title),
                    action = { viewModel.downloadSong(song) })
            }
        }
        viewModel.isLoading.onPropertyChanged { binding.swipeRefreshLayout.isRefreshing = it }
        binding.swipeRefreshLayout.run {
            setOnRefreshListener { viewModel.updateData() }
            setColorSchemeColors(context.color(R.color.accent))
        }
        binding.recyclerView.run {
            setHasFixedSize(true)
            addItemDecoration(SpacesItemDecoration(context.dimension(R.dimen.content_padding)))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    if (dy > 0 && recyclerView?.isAnimating == false) {
                        hideKeyboard(activity?.currentFocus)
                    }
                }
            })
        }
        (view.parent as? ViewGroup)?.run {
            viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver?.removeOnPreDrawListener(this)
                    startPostponedEnterTransition()
                    return true
                }
            })
        }
    }

    override fun setReenterTransition(transition: Any?) {
        super.setReenterTransition(transition)
        (transition as? Transition)?.let {
            it.removeListener(this)
            it.addListener(this)
        }
    }

    override fun onTransitionEnd(transition: Transition?) = binding.recyclerView.invalidateItemDecorations()

    override fun onTransitionResume(transition: Transition?) = Unit

    override fun onTransitionPause(transition: Transition?) = Unit

    override fun onTransitionCancel(transition: Transition?) = Unit

    override fun onTransitionStart(transition: Transition?) = Unit
}