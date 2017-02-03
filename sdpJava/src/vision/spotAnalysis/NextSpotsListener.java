package vision.spotAnalysis;

import vision.colorAnalysis.SDPColor;
import vision.spotAnalysis.approximatedSpotAnalysis.Spot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Simon Rovder
 */
public interface NextSpotsListener {
    void nextSpots(HashMap<SDPColor, ArrayList<Spot>> spots, long time);
}
