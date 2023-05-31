/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction.house;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class House {

    private static int currentItemIdNum = 1;
    private static int currentBidIdNum = 1;
    private final String id;
    private final Map<String, Item> items;
    private boolean updateList;


    public House(String id) {
        this.id = id;

        this.items = new HashMap<>();
        updateList = true;
        fillItemList(3);
    }

    public String getId() {
        return id;
    }

    public String getNextItemId() {
        return String.format("i%02d",
                currentItemIdNum++);
    }

    public String getNextBidId() {
        return String.format("b%02d",
                currentBidIdNum++);
    }

    public Set<String> getItemIds() {
        return items.keySet();
    }

    ///////////////////////////////////////////////////////////////////////////

    public Item getItem(String id) {
        return items.get(id);
    }

    /**
     * generates an item
     */
    public Item generateItem() {
        Random random = new Random();
        int randomPrice = 10 * (random.nextInt(100 - 1) + 1);
        Item item = new Item(randomPrice, getNextItemId(), id);
        return item;
    }

    /**
     * fills the house with items
     *
     * @param numItems how many
     */
    public void fillItemList(int numItems) {
        if (updateList) {
            for (int i = 0; i < numItems; numItems--) {
                addItem(generateItem());
            }
        }
    }

    /**
     * adds an item
     *
     * @param item the item
     */
    public void addItem(Item item) {
        items.put(item.getId(), item);
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void setUpdateList(boolean updateList) {
        this.updateList = updateList;
    }
}

