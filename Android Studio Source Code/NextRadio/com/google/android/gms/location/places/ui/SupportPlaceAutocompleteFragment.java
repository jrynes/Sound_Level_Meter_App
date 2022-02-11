package com.google.android.gms.location.places.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import com.google.android.gms.C0588R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete.IntentBuilder;
import com.google.android.gms.maps.model.LatLngBounds;
import org.apache.activemq.transport.stomp.Stomp;

public class SupportPlaceAutocompleteFragment extends Fragment {
    private View zzaRh;
    private View zzaRi;
    private EditText zzaRj;
    @Nullable
    private LatLngBounds zzaRk;
    @Nullable
    private AutocompleteFilter zzaRl;
    @Nullable
    private PlaceSelectionListener zzaRm;

    /* renamed from: com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment.1 */
    class C09591 implements OnClickListener {
        final /* synthetic */ SupportPlaceAutocompleteFragment zzaRo;

        C09591(SupportPlaceAutocompleteFragment supportPlaceAutocompleteFragment) {
            this.zzaRo = supportPlaceAutocompleteFragment;
        }

        public void onClick(View view) {
            this.zzaRo.zzzG();
        }
    }

    /* renamed from: com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment.2 */
    class C09602 implements OnClickListener {
        final /* synthetic */ SupportPlaceAutocompleteFragment zzaRo;

        C09602(SupportPlaceAutocompleteFragment supportPlaceAutocompleteFragment) {
            this.zzaRo = supportPlaceAutocompleteFragment;
        }

        public void onClick(View view) {
            this.zzaRo.setText(Stomp.EMPTY);
        }
    }

    private void zzzF() {
        int i = 0;
        int i2 = !this.zzaRj.getText().toString().isEmpty() ? 1 : 0;
        View view = this.zzaRi;
        if (i2 == 0) {
            i = 8;
        }
        view.setVisibility(i);
    }

    private void zzzG() {
        int i;
        int connectionStatusCode;
        try {
            startActivityForResult(new IntentBuilder(2).setBoundsBias(this.zzaRk).setFilter(this.zzaRl).zzeq(this.zzaRj.getText().toString()).zzig(1).build(getActivity()), 1);
            i = -1;
        } catch (Throwable e) {
            connectionStatusCode = e.getConnectionStatusCode();
            Log.e("Places", "Could not open autocomplete activity", e);
            i = connectionStatusCode;
        } catch (Throwable e2) {
            connectionStatusCode = e2.errorCode;
            Log.e("Places", "Could not open autocomplete activity", e2);
            i = connectionStatusCode;
        }
        if (i != -1) {
            GoogleApiAvailability.getInstance().showErrorDialogFragment(getActivity(), i, 2);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == -1) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                if (this.zzaRm != null) {
                    this.zzaRm.onPlaceSelected(place);
                }
                setText(place.getName().toString());
            } else if (resultCode == 2) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                if (this.zzaRm != null) {
                    this.zzaRm.onError(status);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(C0588R.layout.place_autocomplete_fragment, container, false);
        this.zzaRh = inflate.findViewById(C0588R.id.place_autocomplete_search_button);
        this.zzaRi = inflate.findViewById(C0588R.id.place_autocomplete_clear_button);
        this.zzaRj = (EditText) inflate.findViewById(C0588R.id.place_autocomplete_search_input);
        OnClickListener c09591 = new C09591(this);
        this.zzaRh.setOnClickListener(c09591);
        this.zzaRj.setOnClickListener(c09591);
        this.zzaRi.setOnClickListener(new C09602(this));
        zzzF();
        return inflate;
    }

    public void onDestroyView() {
        this.zzaRh = null;
        this.zzaRi = null;
        this.zzaRj = null;
        super.onDestroyView();
    }

    public void setBoundsBias(@Nullable LatLngBounds bounds) {
        this.zzaRk = bounds;
    }

    public void setFilter(@Nullable AutocompleteFilter filter) {
        this.zzaRl = filter;
    }

    public void setHint(CharSequence hint) {
        this.zzaRj.setHint(hint);
        this.zzaRh.setContentDescription(hint);
    }

    public void setOnPlaceSelectedListener(PlaceSelectionListener listener) {
        this.zzaRm = listener;
    }

    public void setText(CharSequence text) {
        this.zzaRj.setText(text);
        zzzF();
    }
}
