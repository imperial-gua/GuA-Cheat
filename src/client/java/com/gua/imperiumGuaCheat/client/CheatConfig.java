/*
    This file is part of ImperiumCheat.

    ImperiumCheat is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ImperiumCheat is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with ImperiumCheat.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2026 Impergram & ImpergramX & GuA development team
              2026 Obitocjkiy Gleb || oxxx1mif || oxxx1Dev
*/

package com.gua.imperiumGuaCheat.client;

import java.util.concurrent.CopyOnWriteArrayList;

public class CheatConfig {
    public static boolean TestActive = false;
    public static boolean killAuraActive = false;
    public static boolean hitboxesActive = false;
    public static long lastAttackTime = 0;

    public static class DamagePopUp {
        public double x, y, z;
        public String amount;
        public long spawnTime;

        public DamagePopUp(double x, double y, double z, String amount) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.amount = amount;
            this.spawnTime = System.currentTimeMillis();
        }
    }

    public static CopyOnWriteArrayList<DamagePopUp> damagePopUps = new CopyOnWriteArrayList<>();

    public static void addDamage(double x, double y, double z, double amount) {
        String text = "§c-" + String.format("%.1f", amount);
        damagePopUps.add(new DamagePopUp(x, y + 1.2, z, text));
    }
}
