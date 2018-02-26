package uk.gov.cshr.locationservice.service;

import org.junit.Assert;
import org.testng.annotations.Test;

public class RegionLookupTest {

    @Test
    public void testRegionLookups() {

        Assert.assertEquals(UK_NUTS.UKC, RegionLookup.findRegion(55.2970314, -1.72889996));
        Assert.assertEquals(UK_NUTS.UKD, RegionLookup.findRegion(54.44945145, -2.7723701));
        Assert.assertEquals(UK_NUTS.UKE, RegionLookup.findRegion(53.93264008, -1.28711998));
        Assert.assertEquals(UK_NUTS.UKF, RegionLookup.findRegion(52.79571915, -0.84966999));
        Assert.assertEquals(UK_NUTS.UKG, RegionLookup.findRegion(52.55696869, -2.2035799));
        Assert.assertEquals(UK_NUTS.UKH, RegionLookup.findRegion(52.24066925, 0.50414598));
        Assert.assertEquals(UK_NUTS.UKI, RegionLookup.findRegion(51.49227142, -0.30864));
        Assert.assertEquals(UK_NUTS.UKJ, RegionLookup.findRegion(51.4509697, -0.99311));
        Assert.assertEquals(UK_NUTS.UKK, RegionLookup.findRegion(50.81119156, -3.63343));
        Assert.assertEquals(UK_NUTS.UKL, RegionLookup.findRegion(52.06740952, -3.99415994));
        Assert.assertEquals(UK_NUTS.UKM, RegionLookup.findRegion(56.1774292, -3.97091007));
        Assert.assertEquals(UK_NUTS.UKN, RegionLookup.findRegion(54.61494064, -6.85481024));
        Assert.assertEquals(null, RegionLookup.findRegion(0D, 0D));
    }
}
