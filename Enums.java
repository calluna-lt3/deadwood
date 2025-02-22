public class Enums { 
    public static enum errno {
        BAD_ROLE,
        NO_CREDITS,
        NO_MONEY,
        MAX_RANK,
        OOB,
        LEQ,
        IN_ROLE,
        FORBIDDEN_ACTION,
        BAD_ROOM,
    }

    public static enum action {
        UNKNOWN,
        WHO,
        LOCATION,
        ROLES,
        TAKE_ROLE,
        MOVE,
        UPGRADE,
        ACT,
        REHEARSE,
        PASS,
    }
}

