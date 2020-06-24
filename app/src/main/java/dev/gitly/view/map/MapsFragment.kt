package dev.gitly.view.map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.utils.collection.addMarker
import com.google.maps.android.ktx.utils.geojson.geoJsonLayer
import com.google.maps.android.ktx.utils.kml.kmlLayer
import dev.gitly.R
import dev.gitly.core.util.MyItemReader
import dev.gitly.model.data.MyItem
import org.json.JSONException

class MapsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        lifecycleScope.launchWhenCreated {
            val googleMap = mapFragment?.awaitMap()
            val sydney = LatLng(51.403186, -0.126446)
            googleMap?.addMarker(MarkerOptions().position(sydney).title("Somewhere in England"))
            googleMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireActivity(),
                    R.raw.map_style
                )
            )
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0f))
            if (googleMap != null) showMapLayers(googleMap)
        }
    }

    private fun showMapLayers(map: GoogleMap) {
        // Shared object managers - used to support multiple layer types on the map simultaneously
        val markerManager = MarkerManager(map)
        val groundOverlayManager = GroundOverlayManager(map)
        val polygonManager = PolygonManager(map)
        val polylineManager = PolylineManager(map)

        addClusters(map, markerManager)
        addGeoJson(map, markerManager, polylineManager, polygonManager, groundOverlayManager)
        addKml(map, markerManager, polylineManager, polygonManager, groundOverlayManager)
        addMarker(markerManager)
    }

    private fun addClusters(map: GoogleMap, markerManager: MarkerManager) {
        val clusterManager = ClusterManager<MyItem>(requireContext(), map, markerManager)
        map.setOnCameraIdleListener(clusterManager)

        try {
            val items = MyItemReader().read(resources.openRawResource(R.raw.radar_search))
            clusterManager.addItems(items)
        } catch (e: JSONException) {
            Toast.makeText(requireContext(), "Problem reading list of markers.", Toast.LENGTH_LONG)
                .show()
            e.printStackTrace()
        }
    }

    private fun addGeoJson(
        map: GoogleMap,
        markerManager: MarkerManager,
        polylineManager: PolylineManager,
        polygonManager: PolygonManager,
        groundOverlayManager: GroundOverlayManager
    ) {
        // GeoJSON polyline
        val geoJsonLineLayer = geoJsonLayer(
            map = map,
            resourceId = R.raw.south_london_line_geojson,
            context = requireContext(),
            markerManager = markerManager,
            polygonManager = polygonManager,
            polylineManager = polylineManager,
            groundOverlayManager = groundOverlayManager
        )
        // Make the line red
        geoJsonLineLayer.features.forEach {
            it.lineStringStyle = GeoJsonLineStringStyle().apply {
                color = Color.RED
            }
        }

        geoJsonLineLayer.addLayerToMap()
        geoJsonLineLayer.setOnFeatureClickListener { feature ->
            Toast.makeText(
                requireContext(),
                "GeoJSON polyline clicked: " + feature.getProperty("title"),
                Toast.LENGTH_SHORT
            ).show()
        }

        // GeoJSON polygon
        val geoJsonPolygonLayer = geoJsonLayer(
            map = map,
            resourceId = R.raw.south_london_square_geojson,
            context = requireContext(),
            markerManager = markerManager,
            polygonManager = polygonManager,
            polylineManager = polylineManager,
            groundOverlayManager = groundOverlayManager
        )
        // Fill it with red
        geoJsonPolygonLayer.features.forEach {
            it.polygonStyle = GeoJsonPolygonStyle().apply {
                fillColor = Color.RED
            }
        }

        geoJsonPolygonLayer.addLayerToMap()
        geoJsonPolygonLayer.setOnFeatureClickListener { feature ->
            Toast.makeText(
                requireContext(),
                "GeoJSON polygon clicked: " + feature.getProperty("title"),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addKml(
        map: GoogleMap,
        markerManager: MarkerManager,
        polylineManager: PolylineManager,
        polygonManager: PolygonManager,
        groundOverlayManager: GroundOverlayManager
    ) {
        // KML Polyline
        val kmlPolylineLayer = kmlLayer(
            map = map,
            resourceId = R.raw.south_london_line_kml,
            context = requireContext(),
            markerManager = markerManager,
            polygonManager = polygonManager,
            polylineManager = polylineManager,
            groundOverlayManager = groundOverlayManager,
        )
        kmlPolylineLayer.addLayerToMap()
        kmlPolylineLayer.setOnFeatureClickListener { feature ->
            Toast.makeText(
                requireContext(),
                "KML polyline clicked: " + feature.getProperty("name"),
                Toast.LENGTH_SHORT
            ).show()
        }

        // KML Polygon
        val kmlPolygonLayer = kmlLayer(
            map = map,
            resourceId = R.raw.south_london_square_kml,
            context = requireContext(),
            markerManager = markerManager,
            polygonManager = polygonManager,
            polylineManager = polylineManager,
            groundOverlayManager = groundOverlayManager,
        )
        kmlPolygonLayer.addLayerToMap()
        kmlPolygonLayer.setOnFeatureClickListener { feature ->
            Toast.makeText(
                requireContext(),
                "KML polygon clicked: " + feature.getProperty("name"),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addMarker(markerManager: MarkerManager) {
        // Unclustered marker - instead of adding to the map directly, use the MarkerManager
        val markerCollection: MarkerManager.Collection = markerManager.newCollection()
        markerCollection.addMarker {
            position(LatLng(51.150000, -0.150032))
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            title("Unclustered marker")
        }
        markerCollection.setOnMarkerClickListener { marker ->
            Toast.makeText(
                requireContext(),
                "Marker clicked: " + marker.title,
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }
}