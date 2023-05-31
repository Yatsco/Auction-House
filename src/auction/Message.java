/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction;

public final class Message {
    private final Type type;
    private final String data1;
    private final String data2;
    private final String data3;
    private final String data4;
    public Message(Type type, String data1, String data2,
                   String data3, String data4) {
        this.type = type;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
    }

    public Message(Type type) {
        this(type, "-", "-",
                "-", "-");
    }


    public Message(Type type, String data1) {
        this(type, data1,
                "-", "-", "-");
    }

    public Message(Type type, String data1, String data2) {
        this(type, data1, data2, "-", "-");
    }

    public Message(Type type, String data1, String data2, String data3) {
        this(type, data1, data2, data3, "-");
    }

    public static Type getMessageType(String typeStr) {
        Type type;
        if (typeStr.equalsIgnoreCase(Type.REGISTER_HOUSE.name())) {
            type = Type.REGISTER_HOUSE;
        } else if (typeStr.equalsIgnoreCase(Type.REGISTER_AGENT.name())) {
            type = Type.REGISTER_AGENT;
        } else if (typeStr.equalsIgnoreCase(Type.QUERY_AGENTS.name())) {
            type = Type.QUERY_AGENTS;
        } else if (typeStr.equalsIgnoreCase(Type.QUERY_HOUSES.name())) {
            type = Type.QUERY_HOUSES;
        } else if (typeStr.equalsIgnoreCase(Type.QUERY_HOST.name())) {
            type = Type.QUERY_HOST;
        } else if (typeStr.equalsIgnoreCase(Type.QUERY_ITEMS.name())) {
            type = Type.QUERY_ITEMS;
        } else if (typeStr.equalsIgnoreCase(Type.QUERY_ITEM.name())) {
            type = Type.QUERY_ITEM;
        } else if (typeStr.equalsIgnoreCase(
                Type.QUERY_AGENT_ACCOUNT_BALANCE.name())) {
            type = Type.QUERY_AGENT_ACCOUNT_BALANCE;
        } else if (typeStr.equalsIgnoreCase
                (Type.QUERY_HOUSE_ACCOUNT_BALANCE.name())) {
            type = Type.QUERY_HOUSE_ACCOUNT_BALANCE;
        } else if (typeStr.equalsIgnoreCase(Type.MAKE_BID.name())) {
            type = Type.MAKE_BID;
        } else if (typeStr.equalsIgnoreCase(Type.TRANSFER_FUNDS.name())) {
            type = Type.TRANSFER_FUNDS;
        } else if (typeStr.equalsIgnoreCase(Type.REQUEST_ID.name())) {
            type = Type.REQUEST_ID;
        } else if (typeStr.equalsIgnoreCase(Type.RETURN.name())) {
            type = Type.RETURN;
        } else if (typeStr.equalsIgnoreCase(Type.HOLD_FUNDS.name())) {
            return Type.HOLD_FUNDS;
        } else if (typeStr.equalsIgnoreCase(Type.RELEASE_FUNDS.name())) {
            return Type.RELEASE_FUNDS;
        } else if (typeStr.equalsIgnoreCase(Type.CHECK_WIN.name())) {
            return Type.CHECK_WIN;
        } else if (typeStr.equalsIgnoreCase(Type.ITEM_PAID.name())) {
            return Type.ITEM_PAID;
        } else if (typeStr.equalsIgnoreCase(Type.CLOSE.name())) {
            return Type.CLOSE;
        } else {
            type = Type.UNKNOWN;
        }
        return type;
    }

    public static Message fromString(String s) {
        String[] parts = s.split(";");
        int numParts = parts.length;
        Type type = Type.UNKNOWN;
        String data1 = "-";
        String data2 = "-";
        String data3 = "-";
        String data4 = "-";

        for (int i = 0; i < numParts; i++) {
            if (i == 0) {
                type = getMessageType(parts[0]);
            } else if (i == 1) {
                data1 = parts[1];
            } else if (i == 2) {
                data2 = parts[2];
            } else if (i == 3) {
                data3 = parts[3];
            } else if (i == 4) {
                data4 = parts[4];
            }
        }
        return new Message(type, data1, data2, data3, data4);
    }

    public Type getType() {
        return type;
    }

    public String getData1() {
        return data1;
    }

    public String getData2() {
        return data2;
    }

    public String getData3() {
        return data3;
    }

    public String getData4() {
        return data4;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s",
                type.toString(), data1, data2, data3, data4);
    }

    public enum Type {
        REGISTER_HOUSE,
        REGISTER_AGENT,
        QUERY_AGENTS,
        QUERY_HOUSES,
        QUERY_HOUSE_ACCOUNT_BALANCE,
        QUERY_HOST,
        QUERY_ITEMS,
        QUERY_ITEM,
        QUERY_AGENT_ACCOUNT_BALANCE,
        MAKE_BID,
        HOLD_FUNDS,
        RELEASE_FUNDS,
        CHECK_WIN,
        ITEM_PAID,
        TRANSFER_FUNDS,
        REQUEST_ID,
        RETURN,
        CLOSE,
        UNKNOWN
    }
}