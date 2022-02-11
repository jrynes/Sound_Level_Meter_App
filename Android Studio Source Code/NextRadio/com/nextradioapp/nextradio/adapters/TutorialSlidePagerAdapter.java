package com.nextradioapp.nextradio.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.nextradioapp.nextradio.activities.SplashScreen;
import com.nextradioapp.nextradio.fragments.ProductTourFragment;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class TutorialSlidePagerAdapter extends FragmentStatePagerAdapter {
    public TutorialSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {
        if (SplashScreen.NUM_PAGES == 5) {
            return displayNormalTutorialFragment(position);
        }
        return displayMotorolaTutorialFragment(position);
    }

    public int getCount() {
        return SplashScreen.NUM_PAGES;
    }

    private ProductTourFragment displayNormalTutorialFragment(int position) {
        switch (position) {
            case Tokenizer.EOF /*0*/:
                return ProductTourFragment.newInstance(2130903120, position);
            case Zone.PRIMARY /*1*/:
                return ProductTourFragment.newInstance(2130903121, position);
            case Zone.SECONDARY /*2*/:
                return ProductTourFragment.newInstance(2130903122, position);
            case Protocol.GGP /*3*/:
                return ProductTourFragment.newInstance(2130903124, position);
            case Type.MF /*4*/:
                return ProductTourFragment.newInstance(2130903125, position);
            default:
                return null;
        }
    }

    private ProductTourFragment displayMotorolaTutorialFragment(int position) {
        switch (position) {
            case Tokenizer.EOF /*0*/:
                return ProductTourFragment.newInstance(2130903120, position);
            case Zone.PRIMARY /*1*/:
                return ProductTourFragment.newInstance(2130903121, position);
            case Zone.SECONDARY /*2*/:
                return ProductTourFragment.newInstance(2130903122, position);
            case Protocol.GGP /*3*/:
                return ProductTourFragment.newInstance(2130903123, position);
            case Type.MF /*4*/:
                return ProductTourFragment.newInstance(2130903124, position);
            case Service.RJE /*5*/:
                return ProductTourFragment.newInstance(2130903125, position);
            default:
                return null;
        }
    }
}
