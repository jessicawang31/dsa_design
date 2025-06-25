package seamfinding;

import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        double[][] energy = new double[picture.width()][picture.height()];
        int[][] edgeTo = new int[picture.width()][picture.height()];

        for (int y = 0; y < picture.height(); y++) {
            energy[0][y] = f.apply(picture, 0, y);
        }

        for (int x = 1; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                double up = 0;
                if (y > 0) {
                    up = energy[x - 1][y - 1];
                } else {
                    up = Double.MAX_VALUE;
                }

                double left = energy[x - 1][y];

                double down = 0;
                if (y < picture.height() - 1) {
                    down = energy[x - 1][y + 1];
                } else {
                    down = Double.MAX_VALUE;
                }

                double minEnergy = Math.min(Math.min(up, left), down);
                energy[x][y] = f.apply(picture, x, y) + minEnergy;

                if (minEnergy == left) {
                    edgeTo[x][y] = y;
                } else if (minEnergy == up) {
                    edgeTo[x][y] = y - 1;
                } else {
                    edgeTo[x][y] = y + 1;
                }
            }
        }

        double minTotalCost = Double.MAX_VALUE;
        int minIndex = 0;

        for (int y = 0; y < picture.height(); y++) {
            if (energy[picture.width() - 1][y] < minTotalCost) {
                minTotalCost = energy[picture.width() - 1][y];
                minIndex = y;
            }
        }

        List<Integer> seam = new ArrayList<>();
        for (int x = picture.width() -1; x >= 0; x--) {
            seam.add(minIndex);
            minIndex = edgeTo[x][minIndex];
        }
        Collections.reverse(seam);
        return seam;
    }
}
