package iitevent.project.iit.helpers;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import iitevent.project.iit.activities.R;
import iitevent.project.iit.bean.Event;

/**
 * @author Akshay Patil
 * Custom adapter to display custom list view and perform operations such as select items in the list view.
 */
public class ListViewAdapter extends ArrayAdapter<Event> {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<Event> eventDetailList;
    private SparseBooleanArray mSelectedItemsIds;

    /**
     *
     * @param context Acitivity object from where the adapter was called
     * @param resourceId resource to be used to display the list items
     * @param eventDetailList list of EventDetail objects to be displayed
     */
    public ListViewAdapter(Context context, int resourceId,
                           List<Event> eventDetailList) {
        //calling the parent array adapter class constructor
        super(context, resourceId, eventDetailList);
        //instantiating the SparseBooleanArray object to hold the int boolean values of the list view  items 
        //true being the selected item.
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.eventDetailList = eventDetailList;
        //instantiating the inflater object
        inflater = LayoutInflater.from(context);
    }

    /**
     * @author Akshay Patil
     * Private inner class to create a general object to be set to the textview and image view of the listview item
     */
    private class ViewHolder {
        TextView eventName,eventDate;

    }

    /**
     *
     * @param position position of the item in the list
     * @param view view object
     * @param parent view group object
     * @return modified view to be displayed
     */
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        //if the view is null we set a tag to the view and get it if the view is not null
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.event_item, null);
            // Locate the TextViews in row_items
            holder.eventName = (TextView) view.findViewById(R.id.eventName);
            // Locate the ImageView in row_items
            holder.eventDate = (TextView) view.findViewById(R.id.eventDate);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Capture position and set to the TextViews
        holder.eventName.setText(eventDetailList.get(position).getEventName());

        // Capture position and set to the ImageView
        holder.eventDate.setText(eventDetailList.get(position).getEventDate());
        return view;
    }

    @Override
    /**
     * Removes the QuoteDetail object from the Adapter list
     */
    public void remove(Event object) {
        eventDetailList.remove(object);
        notifyDataSetChanged();
    }

    /**
     * Gets quote Detail list
     * @return quoteDetail list
     */
    public List<Event> getEventDetailList() {
        return eventDetailList;
    }

    /**
     * makes changes to the SparseBooleanArray object each time the user selects or deselects the list view item
     * @param position position of the item selected or deselected
     */
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    /**
     * creates a fresh instance of the SparseBooleanArray
     */
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    /**
     * Selects the list view item by modifying the SparseBooleanArray objects value for the particular position
     * Finally provides only the list of items seleced.
     * @param position item selected or deselected position
     * @param value boolean value to be added for a particular position.
     */
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    /**
     * gets the count of the items selected
     * @return integer value of the total items selected which is the size of the SparseBooleanArray object
     */
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    /**
     * Returns the SparseBooleanArray object
     * @return SparseBooleanArray object
     */
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
