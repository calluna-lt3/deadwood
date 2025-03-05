public class Enums { 
    public static enum errno {
        NO_CREDITS,
        NO_MONEY,
        MAX_RANK,
        OOB,
        LEQ,
        IN_ROLE,
        FORBIDDEN_ACTION,
        BAD_ROOM,
        BAD_ARGS,
        DUP_ACTION,
    }

    public static enum action {
        UNKNOWN,
        HELP,
        WHO,
        LOCATION,
        ROOMS,
        ROLES,
        TAKE_ROLE,
        MOVE,
        UPGRADE,
        ACT,
        REHEARSE,
        PASS,
    }
}

