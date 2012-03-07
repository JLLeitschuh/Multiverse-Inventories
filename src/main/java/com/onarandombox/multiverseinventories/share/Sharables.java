package com.onarandombox.multiverseinventories.share;

import com.onarandombox.multiverseinventories.api.DataStrings;
import com.onarandombox.multiverseinventories.api.profile.PlayerProfile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Sharables implements Shares {

    private static Shares allSharables = new Sharables(new LinkedHashSet<Sharable>());
    static Map<String, Shares> lookupMap = new HashMap<String, Shares>();

    /**
     * Sharing Inventory.
     */
    public static final Sharable<ItemStack[]> INVENTORY = new AbstractSharable<ItemStack[]>("inventory_contents",
            new ProfileEntry(false, DataStrings.PLAYER_INVENTORY_CONTENTS)) {
        @Override
        public void updateProfile(PlayerProfile profile, Player player) {
            profile.setInventoryContents(player.getInventory().getContents());
        }

        @Override
        public void updatePlayer(Player player, PlayerProfile profile) {
            player.getInventory().setContents(profile.getInventoryContents());
        }
    };

    /**
     * Sharing Armor.
     */
    public static final Sharable<ItemStack[]> ARMOR = new AbstractSharable<ItemStack[]>("armor_contents",
            new ProfileEntry(false, DataStrings.PLAYER_ARMOR_CONTENTS), "armor") {
        @Override
        public void updateProfile(PlayerProfile profile, Player player) {
            profile.setArmorContents(player.getInventory().getArmorContents());
        }

        @Override
        public void updatePlayer(Player player, PlayerProfile profile) {
            player.getInventory().setArmorContents(profile.getArmorContents());

        }
    };

    /**
     * Sharing Health.
     */
    public static final Sharable<Integer> HEALTH = new AbstractSharable<Integer>("health",
            new ProfileEntry(true, DataStrings.PLAYER_HEALTH), "hp", "hitpoints", "hit_points") {
        @Override
        public void updateProfile(PlayerProfile profile, Player player) {
            profile.setHealth(player.getHealth());
        }

        @Override
        public void updatePlayer(Player player, PlayerProfile profile) {
            player.setHealth(profile.getHealth());
        }
    };

    /**
     * Sharing Experience.
     */
    public static final Sharable<Float> EXPERIENCE = new AbstractSharable<Float>("exp",
            new ProfileEntry(true, DataStrings.PLAYER_EXPERIENCE), "xp") {
        @Override
        public void updateProfile(PlayerProfile profile, Player player) {
            profile.setExp(player.getExp());
        }

        @Override
        public void updatePlayer(Player player, PlayerProfile profile) {
            player.setExp(profile.getExp());
        }
    };

    /**
     * Sharing Experience.
     */
    public static final Sharable<Integer> LEVEL = new AbstractSharable<Integer>("level",
            new ProfileEntry(true, DataStrings.PLAYER_LEVEL), "lvl") {
        @Override
        public void updateProfile(PlayerProfile profile, Player player) {
            profile.setLevel(player.getLevel());
        }

        @Override
        public void updatePlayer(Player player, PlayerProfile profile) {
            player.setLevel(profile.getLevel());
        }
    };

    /**
     * Sharing Experience.
     */
    public static final Sharable<Integer> TOTAL_EXPERIENCE = new AbstractSharable<Integer>("total_exp",
            new ProfileEntry(true, DataStrings.PLAYER_TOTAL_EXPERIENCE), "total_xp", "totalxp") {
        @Override
        public void updateProfile(PlayerProfile profile, Player player) {
            profile.setTotalExperience(player.getTotalExperience());
        }

        @Override
        public void updatePlayer(Player player, PlayerProfile profile) {
            player.setTotalExperience(profile.getTotalExperience());
        }
    };

    /**
     * Sharing Hunger.
     */
    public static final Sharable<Integer> FOOD_LEVEL = new AbstractSharable<Integer>("food_level",
            new ProfileEntry(true, DataStrings.PLAYER_FOOD_LEVEL), "food") {
        @Override
        public void updateProfile(PlayerProfile profile, Player player) {
            profile.setFoodLevel(player.getFoodLevel());
        }

        @Override
        public void updatePlayer(Player player, PlayerProfile profile) {
            player.setFoodLevel(profile.getFoodLevel());
        }
    };

    /**
     * Sharing Hunger.
     */
    public static final Sharable<Float> EXHAUSTION = new AbstractSharable<Float>("exhaustion",
            new ProfileEntry(true, DataStrings.PLAYER_EXHAUSTION), "exhaust", "exh") {
        @Override
        public void updateProfile(PlayerProfile profile, Player player) {
            profile.setExhaustion(player.getExhaustion());
        }

        @Override
        public void updatePlayer(Player player, PlayerProfile profile) {
            player.setExhaustion(profile.getExhaustion());
        }
    };

    /**
     * Sharing Hunger.
     */
    public static final Sharable<Float> SATURATION = new AbstractSharable<Float>("saturation",
            new ProfileEntry(true, DataStrings.PLAYER_SATURATION), "sat") {
        @Override
        public void updateProfile(PlayerProfile profile, Player player) {
            profile.setSaturation(player.getSaturation());
        }

        @Override
        public void updatePlayer(Player player, PlayerProfile profile) {
            player.setSaturation(profile.getSaturation());
        }
    };

    /**
     * Sharing Bed Spawn.
     */
    public static final Sharable<Location> BED_SPAWN = new AbstractSharable<Location>("bed_spawn",
            new ProfileEntry(true, DataStrings.PLAYER_SATURATION), "bedspawn", "bed", "beds") {
        @Override
        public void updateProfile(PlayerProfile profile, Player player) {
            profile.setBedSpawnLocation(player.getBedSpawnLocation());
        }

        @Override
        public void updatePlayer(Player player, PlayerProfile profile) {
            Location loc = profile.getBedSpawnLocation();
            if (loc == null) {
                loc = player.getWorld().getSpawnLocation();
            }
            player.setBedSpawnLocation(loc);
        }
    };

    public static final SharableGroup ALL_INVENTORY = new SharableGroup("inventory",
            fromSharables(INVENTORY, ARMOR), "inv", "inventories");

    public static final SharableGroup ALL_EXPERIENCE = new SharableGroup("experience",
            fromSharables(EXPERIENCE, TOTAL_EXPERIENCE, LEVEL));

    public static final SharableGroup HUNGER = new SharableGroup("hunger",
            fromSharables(FOOD_LEVEL, SATURATION, EXHAUSTION));

    public static final SharableGroup STATS = new SharableGroup("stats",
            fromSharables(HEALTH, FOOD_LEVEL, SATURATION, EXHAUSTION, EXPERIENCE, TOTAL_EXPERIENCE, LEVEL));

    public static final SharableGroup ALL_DEFAULT = new SharableGroup("all", fromSharables(HEALTH,
            FOOD_LEVEL, SATURATION, EXHAUSTION, EXPERIENCE, TOTAL_EXPERIENCE, LEVEL, INVENTORY, ARMOR, BED_SPAWN), "*",
            "everything");

    public static boolean register(Sharable sharable) {
        if (allSharables.add(sharable)) {
            for (String name : sharable.getNames()) {
                String key = name.toLowerCase();
                Shares shares = lookupMap.get(key);
                if (shares == null) {
                    shares = noneOf();
                    lookupMap.put(key, shares);
                }
                shares.add(sharable);
            }
            return true;
        }
        return false;
    }

    /**
     * Looks up a sharable by one of the acceptable names.
     *
     * @param name Name to look up by.
     * @return Sharable by that name or null if none by that name.
     */
    public static Shares lookup(String name) {
        return lookupMap.get(name.toLowerCase());
    }

    public static Shares all() {
        return allSharables;
    }

    public static Shares allOf() {
        return new Sharables(new LinkedHashSet<Sharable>(allSharables));
    }

    public static Shares noneOf() {
        return new Sharables(new LinkedHashSet<Sharable>(allSharables.size()));
    }

    public static Shares complementOf(Shares shares) {
        Set<Sharable> compliment = Sharables.allOf();
        compliment.removeAll(shares);
        return new Sharables(compliment);
    }

    public static Shares fromShares(Shares shares) {
        return new Sharables(shares);
    }

    public static Shares fromCollection(Collection<Sharable> sharesCollection) {
        Shares shares = noneOf();
        shares.addAll(sharesCollection);
        return shares;
    }

    public static Shares fromSharables(Sharable... sharables) {
        Shares shares = noneOf();
        shares.addAll(Arrays.asList(sharables));
        return shares;
    }

    public static Shares fromList(List sharesList) {
        Shares shares = noneOf();
        for (Object shareStringObj : sharesList) {
            String shareString = shareStringObj.toString();
            Shares sharables = Sharables.lookup(shareString);
            if (sharables != null) {
                shares.mergeShares(sharables);
            } else {
                if (shareString.equals("*") || shareString.equalsIgnoreCase("all")
                        || shareString.equalsIgnoreCase("everything")) {
                    shares = allOf();
                    break;
                }
            }
        }
        return shares;
    }

    protected Set<Sharable> sharables;

    private Sharables(Set<Sharable> sharableSet) {
        this.sharables = sharableSet;
    }

    private Sharables(Shares shares) {
        this.sharables = new LinkedHashSet<Sharable>(allSharables.size());
        this.sharables.addAll(shares);
    }

    @Override
    public int size() {
        return this.sharables.size();
    }

    @Override
    public boolean isEmpty() {
        return this.sharables.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.sharables.contains(o);
    }

    @Override
    public Iterator<Sharable> iterator() {
        return this.sharables.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.sharables.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.sharables.toArray(a);
    }

    @Override
    public boolean add(Sharable sharable) {
        return this.sharables.add(sharable);
    }

    @Override
    public boolean remove(Object o) {
        return this.sharables.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.sharables.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Sharable> c) {
        return this.sharables.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.sharables.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.sharables.removeAll(c);
    }

    @Override
    public void clear() {
        this.sharables.clear();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Shares && ((Shares) o).isSharing(this);
    }

    @Override
    public int hashCode() {
        return this.sharables.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeShares(Shares newShares) {
        this.addAll(newShares);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSharing(Sharable sharable, boolean sharing) {
        if (sharing) {
            this.add(sharable);
        } else {
            this.remove(sharable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSharing(Shares sharables, boolean sharing) {
        for (Sharable sharable : sharables) {
            if (sharing) {
                this.add(sharable);
            } else {
                this.remove(sharable);
            }
        }
    }

    @Override
    public Shares compare(Shares shares) {
        Shares bothSharing = Sharables.noneOf();
        for (Sharable sharable : shares) {
            if (this.contains(sharable)) {
                bothSharing.add(sharable);
            }
        }
        return bothSharing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSharing(Sharable sharable) {
        return this.contains(sharable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSharing(Shares shares) {
        boolean isSharing = this.sharables.equals(shares);
        if (!isSharing) {
            for (Sharable sharable : shares) {
                if (!this.isSharing(sharable)) {
                    return false;
                }
            }
            isSharing = true;
        }
        return isSharing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> toStringList() {
        List<String> list = new LinkedList<String>();
        if (this.isSharing(Sharables.allOf())) {
            list.add("all");
        } else {
            for (Sharable sharable : this) {
                list.add(sharable.toString());
            }
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Sharable sharable : this) {
            if (!stringBuilder.toString().isEmpty()) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(sharable);
        }
        return stringBuilder.toString();
    }
}
