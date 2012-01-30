package gov.hhs.fha.nhinc.kmr2.simulatorAgent;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfigDAO {


    public static List search() {
        ArrayList ans = new ArrayList();

        ans.add( new ConfigInfo( "1", "test", "sotty", "describe here",
                new Date(), new Date(), new Date(),
                "test goal", 6042.0,
                new Date(), new Date(), 0L ) );

        return ans;
    }


    public static List searchResults() {
        ArrayList ans = new ArrayList();

        ans.add( new ResultInfo( ) );

        return ans;
    }

    public static Configuration getConfiguration( String configId, String modelId, boolean def ) {
        return new Configuration( modelId, configId );
    }

    public static String saveConfiguration( String configId, String modelId, Configuration config ) {
        System.err.println( "DAO SAVING " + config );
        return configId;
    }


    public static Results getResults( String resultId ) {
        return new Results( resultId );
    }
}
