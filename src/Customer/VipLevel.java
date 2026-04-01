package Customer;

public enum VipLevel {
    BRONZE(0),
    SILVER(5),
    GOLD(10),
    PLATINUM(15);

    private int salePercentage;

    VipLevel(int salePercentage) {
        this.salePercentage = salePercentage;
    }

    public static int getPercentage(int level) {
        return VipLevel.values()[level - 1].salePercentage;
    }

    public static VipLevel getVipLevel(int percentage) {
        for (VipLevel vipLevel : VipLevel.values()) {
            if (vipLevel.salePercentage == percentage) return vipLevel;
        }
        return null;
    }

}
