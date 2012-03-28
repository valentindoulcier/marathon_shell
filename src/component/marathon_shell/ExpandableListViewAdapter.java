package component.marathon_shell;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter  	{
	private ArrayList<String> parent;
	private ArrayList<ArrayList<String>> enfant;
	@SuppressWarnings("unused")
	private LayoutInflater inflater;
	private Context context;
	
	public ExpandableListViewAdapter(ArrayList<String> tabParent, ArrayList<ArrayList<String>> tabEnfant, Context cxt){
		this.parent = tabParent;
		this.enfant = tabEnfant;
		this.context = cxt;
		this.inflater = LayoutInflater.from(context);
	}

	public Object getChild(int levelNum, int childNum) {
		// TODO Auto-generated method stub
		return enfant.get(levelNum).get(childNum);
	}

	public long getChildId(int levelNum, int childNum) {
		// TODO Auto-generated method stub
		return childNum;
	}
	
	/**
	 * @return the enfant
	 */
	public ArrayList<ArrayList<String>> getEnfant() {
		return enfant;
	}

	/**
	 * @param enfant the enfant to set
	 */
	public void setEnfant(String enf) {
		enfant.get(parent.size() - 1).add(enf);
	}


	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		TextView textView = getGenericTextView();
		textView.setTextSize(18);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		
		String nom = getChild(groupPosition, childPosition).toString();
		nom = nom.substring(0, nom.indexOf(".xml"));
		String splitNom [] = nom.split("_");
		String splitDate [] = splitNom[1].split("-");
		String splitHeure [] = splitNom[2].split("-");
		
        textView.setText(splitNom[0] + " du " + splitDate[2] + " " + splitDate[1] + " " + splitDate[0] + " à " + splitHeure[0] + "h" + splitHeure[1] + "m" + splitHeure[2] + "s");
        return textView;
	}

	public int getChildrenCount(int levelNum) {
		// TODO Auto-generated method stub
        int Cpt = enfant.get(levelNum).size();
        return Cpt;
	}

	public Object getGroup(int levelNum) {
		// TODO Auto-generated method stub
		return parent.get(levelNum);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return parent.size();
	}

	public long getGroupId(int levelNum) {
		// TODO Auto-generated method stub
		return levelNum;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup viewGroup) {
		TextView textView = getGenericTextView();
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setTextSize(18);
		textView.setPadding(75, 0, 0, 0);
        textView.setText(getGroup(groupPosition).toString());
        return textView;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	public TextView getGenericTextView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60);
        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        return textView;
    }

}
