package tptests;

import org.junit.*;
import static org.junit.Assert.*;

import participant.TPPath;
import participant.Vehicle;
import street.Lane;
/**
 *
 * @author Christoph Kroell
 */
public class TrafficParticipantTests {
    
    private final StreetNetwork sn = new StreetNetwork();
    
    private final Vehicle[] vehicles = new Vehicle[]
            {
            new Vehicle(2, sn.getLane(0), sn.getLane(3)),
            };
    
    @Test
    public void TPPath() {
        assertArrayEquals(TPPath.get(sn.getLanes().get(0), sn.getLanes().get(3)).toArray(new Lane[0]), new Lane[]{sn.getLane(0), sn.getLane(4), sn.getLane(7)});
    }
    
    @Test
    public void sizeTPList() {
        sn.getLane(0).getTrafficParticipants().add(new Vehicle(1, sn.getLane(0), sn.getLane(20)), 4);
        sn.getLane(0).getTrafficParticipants().add(new Vehicle(1, sn.getLane(0), sn.getLane(18)), 7);
        assertEquals(2, sn.getLane(0).getTrafficParticipants().getSize());
    }
}
