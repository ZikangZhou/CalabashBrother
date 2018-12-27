import algorithm.ShortestPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ShortestPathTest {
    @Test
    public void testPath() {
        ShortestPath sp = new ShortestPath();
        ShortestPath.Graph<Character> graph = sp.new Graph<>();
        graph.add('s', Arrays.asList(sp.new Edge<>('t', 10.0),
                sp.new Edge<>('y', 5.0)));
        graph.add('t', Arrays.asList(sp.new Edge<>('x', 1.0),
                sp.new Edge<>('y', 2.0)));
        graph.add('y', Arrays.asList(sp.new Edge<>('t', 3.0),
                sp.new Edge<>('x', 9.0), sp.new Edge<>('z', 2.0)));
        graph.add('z', Arrays.asList(sp.new Edge<>('s', 7.0),
                sp.new Edge<>('x', 6.0)));
        graph.add('x', Arrays.asList(sp.new Edge<>('z', 4.0)));
        Object[] path = graph.getShortestPath('s', 't').toArray();
        Assert.assertArrayEquals(path, new Character[]{'t', 'y'});
    }
}
