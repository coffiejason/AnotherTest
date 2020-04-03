package com.figure.anothertest;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class TPClusterRenderer extends DefaultClusterRenderer {
    private Context c;
    TPClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        c = context;
    }

    @Override
    protected void onBeforeClusterRendered(Cluster cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);

        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(new Functions().layoutToBitmap(R.layout.clustericon,c)));
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.manicon));

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(new Functions().layoutToBitmap(R.layout.post_icon,c)));
    }
}
