//COMP 4521     Name: Alfonso Miguel Pascual Santos-Tankia      Student ID: 20531732          Email: amsaa@connect.ust.hk
//COMP 4521     Name: Timothy Jacob Regan                       Student ID: 20531756          Email: tjregan@connect.ust.hk

package hk.ust.cse.comp4521.pegjump;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by timre on 4/15/2018.
 */

public class WinningScreen extends Fragment {

    public WinningScreen() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pause_menu, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
