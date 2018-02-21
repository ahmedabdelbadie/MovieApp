package com.example.ahmed.movieapp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    // The minimum number of items to have below your current scroll position
	// before loading more.
	private int visibleThreshold = 5;
	// The current offset index of data you have loaded
	private int currentPage = 0;
	// The total number of items in the dataset after the last load
	private int previousTotalItemCount = 0;
	// True if we are still waiting for the last set of data to load.
	private boolean loading = true;
	// Sets the starting page index
	private int startingPageIndex = 0;

	public EndlessScrollListener() {
	}

	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	public EndlessScrollListener(int visibleThreshold, int startPage) {
		this.visibleThreshold = visibleThreshold;
		this.startingPageIndex = startPage;
		this.currentPage = startPage;
	}

	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
        {
		// If the total item count is zero and the previous isn't, assume the
		// list is invalidated and should be reset back to initial state
		if (totalItemCount < previousTotalItemCount) {
			this.currentPage = this.startingPageIndex;
			this.previousTotalItemCount = totalItemCount;
			if (totalItemCount == 0) { this.loading = true; } 
		}
		// If it's still loading, we check to see if the dataset count has
		// changed, if so we conclude it has finished loading and update the current page
		// number and total item count.
		if (loading && (totalItemCount > previousTotalItemCount)) {
			loading = false;
			previousTotalItemCount = totalItemCount;
			currentPage++;
		}
		
		// If it isn't currently loading, we check to see if we have breached
		// the visibleThreshold and need to reload more data.
		// If we do need to reload some more data, we execute onLoadMore to fetch the data.
		if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount ) {
		    loading = onLoadMore(currentPage + 1, totalItemCount);
		}
	}

	// Defines the process for actually loading more data based on page
	// Returns true if more data is being loaded; returns false if there is no more data to load.
	public abstract boolean onLoadMore(int page, int totalItemsCount);

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// Don't take any action on changed
	}
}

Notice that this is an abstract class, and that in order to use this, you must extend this base class and define the onLoadMore method to actually retrieve the new data. We can define now an anonymous class within any activity that extends EndlessScrollListener and bind that to the AdapterView. For example:

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ... the usual 
        ListView lvItems = (ListView) findViewById(R.id.lvItems);
        // Attach the listener to the AdapterView onCreate
        lvItems.setOnScrollListener(new EndlessScrollListener() {
          @Override
          public boolean onLoadMore(int page, int totalItemsCount) {
              // Triggered only when new data needs to be appended to the list
              // Add whatever code is needed to append new items to your AdapterView
              loadNextDataFromApi(page); 
              // or loadNextDataFromApi(totalItemsCount); 
              return true; // ONLY if more data is actually being loaded; false otherwise.
          }
        });
    }
    

   // Append the next page of data into the adapter
   // This method probably sends out a network request and appends new data items to your adapter. 
   public void loadNextDataFromApi(int offset) {
      // Send an API request to retrieve appropriate paginated data 
      //  --> Send the request including an offset value (i.e `page`) as a query parameter.
      //  --> Deserialize and construct new model objects from the API response
      //  --> Append the new data objects to the existing set of items inside the array of items
      //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
   }
}

Now as you scroll, items will be automatically filling in because the onLoadMore method will be triggered once the user crosses the visibleThreshold. This approach works equally well for a GridView and the listener gives access to both the page as well as the totalItemsCount to support both pagination and offset based fetching.
Implementing with RecyclerView

We can use a similar approach with the RecyclerView by defining an interface EndlessRecyclerViewScrollListener that requires an onLoadMore() method to be implemented. The LayoutManager, which is responsible in the RecyclerView for rendering where items should be positioned and manages scrolling, provides information about the current scroll position relative to the adapter. For this reason, we need to pass an instance of what LayoutManager is being used to collect the necessary information to ascertain when to load more data.

Implementing endless pagination for RecyclerView requires the following steps:

    Copy over the EndlessRecyclerViewScrollListener.java into your application.
    Call addOnScrollListener(...) on a RecyclerView to enable endless pagination. Pass in an instance of EndlessRecyclerViewScrollListener and implement the onLoadMore which fires whenever a new page needs to be loaded to fill up the list.
    Inside the aforementioned onLoadMore method, load additional items into the adapter either by sending out a network request or by loading from another source.

To start handling the scroll events for steps 2 and 3, we need to use the addOnScrollListener() method in our Activity or Fragment and pass in the instance of the EndlessRecyclerViewScrollListener with the layout manager as shown below:

public class MainActivity extends Activity {
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // Configure the RecyclerView
       RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvContacts);
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       rvItems.setLayoutManager(linearLayoutManager);
       // Retain an instance so that you can call `resetState()` for fresh searches
       scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
           @Override
           public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
               // Triggered only when new data needs to be appended to the list
               // Add whatever code is needed to append new items to the bottom of the list
               loadNextDataFromApi(page);
           }
      };
      // Adds the scroll listener to RecyclerView
      rvItems.addOnScrollListener(scrollListener);
  }

  // Append the next page of data into the adapter
  // This method probably sends out a network request and appends new data items to your adapter. 
  public void loadNextDataFromApi(int offset) {
      // Send an API request to retrieve appropriate paginated data 
      //  --> Send the request including an offset value (i.e `page`) as a query parameter.
      //  --> Deserialize and construct new model objects from the API response
      //  --> Append the new data objects to the existing set of items inside the array of items
      //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
  }
}