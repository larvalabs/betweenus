package com.larvalabs.betweenus;

import android.content.Context;

import com.larvalabs.betweenus.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 */
public class Units {

    private static DecimalFormat distanceFormat = new DecimalFormat("###,##0");
    private static Random random = new Random();

    private static final double MIN_DESIREABLE_DIST = 1d;

    public enum Unit {
        MARS(6794d, R.drawable.bu_inapp_units_01, R.string.mars_caption, R.string.mars_size, R.array.frames_mars, R.drawable.bu_units_01, "diameter of mars"),
        MOON(3474d, R.drawable.bu_inapp_units_02, R.string.moon_caption, R.string.moon_size, R.array.frames_moon, R.drawable.bu_units_02, "diameter of the moon"),
        PLUTO(2370d, R.drawable.bu_inapp_units_03, R.string.pluto_caption, R.string.pluto_size, R.array.frames_pluto, R.drawable.bu_units_03, "diameter of pluto"),
        TOUR_FRANCE(152d, R.drawable.bu_inapp_units_04, R.string.tour_france_caption, R.string.tour_france_size, R.array.frames_bike, R.drawable.bu_units_04, "length of a stage in tour de france"),
        LOCH_NESS(35d, R.drawable.bu_inapp_units_05, R.string.loch_ness_caption, R.string.loch_ness_size, R.array.frames_lochness, R.drawable.bu_units_05, "length of loch ness monster"),
        MOUNTAIN(8.849868d, R.drawable.bu_inapp_units_06, R.string.mountain_caption, R.string.mountain_size, R.array.frames_mountain, R.drawable.bu_units_06, "height of mt everest"),
        GOLDEN_GATE_BRIDGE(2.81d, R.drawable.bu_inapp_units_07, R.string.golden_gate_bridge_caption, R.string.golden_gate_bridge_size, R.array.frames_bridge, R.drawable.bu_units_07, "length of golden gate bridge"),
        EMPIRE_STATE_BUILDING(0.4d, R.drawable.bu_inapp_units_08, R.string.empire_state_building_caption, R.string.empire_state_building_size, R.array.frames_empirestate, R.drawable.bu_units_08, "height of empire state building"),
        EIFFEL_TOWER(0.32d, R.drawable.bu_inapp_units_09, R.string.eiffel_tour_caption, R.string.eiffel_tour_size, R.array.frames_paris, R.drawable.bu_units_09, "height of eiffel tower"),
        PYRAMID_GIZA(0.144d, R.drawable.bu_inapp_units_10, R.string.pyramid_giza_caption, R.string.pyramid_giza_size, R.array.frames_pyramid, R.drawable.bu_units_10, "height of the great pyramid at giza"),
        HYPERION_TREE(0.1126d, R.drawable.bu_inapp_units_11, R.string.hyperion_tree_caption, R.string.hyperion_tree_size, R.array.frames_tree, R.drawable.bu_units_11, "height of the hyperion tree"),
        SOCCER_FIELD(0.08d, R.drawable.bu_inapp_units_12, R.string.soccer_field_caption, R.string.soccer_field_size, R.array.frames_soccer, R.drawable.bu_units_12, "length of a soccer field"),
        COMMERCIAL_AIRPLANE(0.037d, R.drawable.bu_inapp_units_airplane, R.string.commercial_airplane_caption, R.string.commercial_airplane_size, R.array.frames_plane, R.drawable.bu_units_airplane, "length of a commercial airplane"),
        WHALE(0.03d, R.drawable.bu_inapp_units_whale, R.string.whale_caption, R.string.whale_size, R.array.frames_whale, R.drawable.bu_units_whale, "length of a blue whale"),
        WORLD_LARGEST_PIZZA(0.03d, R.drawable.bu_inapp_units_13, R.string.largest_pizza_caption, R.string.largest_pizza_size, R.array.frames_pizza, R.drawable.bu_units_13, "world's largest pizza"),
        SAGUARO_CACTUS(0.016d, R.drawable.bu_inapp_units_14, R.string.saguaro_cactus_caption, R.string.saguaro_cactus_size, -1, R.drawable.bu_units_14, "height of saguaro cactus"),
        GIRAFFE(0.0055d, R.drawable.bu_inapp_units_15, R.string.giraffe_caption, R.string.giraffe_size, R.array.frames_giraffe, R.drawable.bu_units_15, "average height of giraffe"),
        MOAI(0.004d, R.drawable.bu_inapp_units_easterhead, R.string.easterhead_caption, R.string.easterhead_size, R.array.frames_moai, R.drawable.bu_units_easterhead, "height of a Moai"),
        CAR(0.00475d, R.drawable.bu_inapp_units_16, R.string.car_caption, R.string.car_size, R.array.frames_car, R.drawable.bu_units_16, "average length of a car"),
        STOPSIGN(0.0025d, R.drawable.bu_inapp_units_stopsign, R.string.stopsign_caption, R.string.stopsign_size, R.array.frames_stopsign, R.drawable.bu_units_stopsign, "height of a stop sign"),
        BASKETBALL(0.00072d, R.drawable.bu_inapp_units_17, R.string.basketball_caption, R.string.basketball_size, R.array.frames_basketball, R.drawable.bu_units_17, "diameter of a basketball"),
        WATCH(0.00025d, R.drawable.bu_inapp_units_watch, R.string.watch_caption, R.string.watch_size, R.array.frames_watch, R.drawable.bu_units_watch, "length of a wristwatch"),
        PHONE(0.00016d, R.drawable.bu_inapp_units_phone, R.string.phone_caption, R.string.phone_size, R.array.frames_phone, R.drawable.bu_units_phone, "dimensions of the Nexus 6P"),
        PENCIL(0.00016d, R.drawable.bu_inapp_units_18, R.string.pencil_caption, R.string.pencil_size, R.array.frames_pencil, R.drawable.bu_units_18, "average width of a pencil"),
        PAPERCLIP(0.00004d, R.drawable.bu_inapp_units_paperclip, R.string.paperclip_caption, R.string.paperclip_size, -1, R.drawable.bu_units_paperclip, "length of a paper clip");

        private double sizeInKm;
        private int imageResourceId;
        private int framesArrayResourceId;
        private int captionStrId;
        private int sizeStrId;
        private int defaultResourceId;
        private String searchQuery;

        Unit(double sizeInKm, int imageResourceId, int captionStrId, int sizeStrId, int framesArrayResourceId, int defaultResourceId, String searchQuery) {
            this.sizeInKm = sizeInKm;
            this.imageResourceId = imageResourceId;
            this.framesArrayResourceId = framesArrayResourceId;
            this.captionStrId = captionStrId;
            this.sizeStrId = sizeStrId;
            this.defaultResourceId = defaultResourceId;
            this.searchQuery = searchQuery;
        }

        public double getSizeInKm() {
            return sizeInKm;
        }

        public int getDefaultResourceId() {
            return defaultResourceId;
        }

        public int getImageResourceId() {
            return imageResourceId;
        }

        public int getFramesArrayResourceId() {
            return framesArrayResourceId;
        }

        public double getDistance(double km) {
            return km / sizeInKm;
        }

        public String getFormattedDistance(double km) {
            double distance = getDistance(km);
            if (distance >= 10) {
                distance = Math.round(distance);
            }
            return distanceFormat.format(distance);
        }

        public int getCaptionStrId() {
            return captionStrId;
        }

        public String getSizeString(Context context) {
            boolean standard = new AppSettings(context).shouldUseStandardUnits();
            String string = context.getResources().getString(sizeStrId);
//            Utils.log("Distance str: " + string);
            String[] split = string.split("\\|");
//            Utils.log("Distance str imperial: " + split[0]);
            if (standard) {
                return split[0];
            } else {
                return split[1];
            }
        }

        public String getSearchQuery() {
            return searchQuery;
        }
    }

    public static Unit getRandomUnit() {
        int index = random.nextInt(Unit.values().length);
        return Unit.values()[index];
    }

    public static List<Unit> getRandomSetOfThreeUnits(final double distanceKm) {
        ArrayList<Unit> allUnits = new ArrayList<>(Arrays.asList(Unit.values()));
        Collections.sort(allUnits, new Comparator<Unit>() {
            @Override
            public int compare(Unit unit, Unit unit2) {
                return Double.compare(unit.getDistance(distanceKm), unit2.getDistance(distanceKm));
            }
        });
        int minIndex = 0;
        for (int i = 0; i < allUnits.size(); i++) {
            Unit unit = allUnits.get(i);
            double distance = unit.getDistance(distanceKm);
            if (distance >= MIN_DESIREABLE_DIST) {
                minIndex = i;
                break;
            }
        }
        int numNonZeroUnits = allUnits.size() - minIndex;
        if (numNonZeroUnits < 3) {
            minIndex = allUnits.size() - 3;
        }
        ArrayList<Unit> randomUnits = new ArrayList<>();
        while (randomUnits.size() < 3) {
            randomUnits.add(allUnits.remove(random.nextInt(allUnits.size() - minIndex) + minIndex));
        }
        Collections.sort(randomUnits, new Comparator<Unit>() {
            @Override
            public int compare(Unit unit, Unit unit2) {
                return Double.compare(unit.getDistance(distanceKm), unit2.getDistance(distanceKm));
            }
        });
/*
        randomUnits.clear();
        randomUnits.add(Unit.BASKETBALL);
        randomUnits.add(Unit.WATCH);
        randomUnits.add(Unit.PENCIL);
*/
        return randomUnits;
    }
}
