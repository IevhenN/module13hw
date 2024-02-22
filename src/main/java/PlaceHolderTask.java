import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class PlaceHolderTask {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        PlaceHolder placeHolder = new PlaceHolder();
        int testId = 4;
        String testUserName = "Leanne Graham";

//      TASK 1
//      1.
        System.out.println("TASK 1.1============================================================================");
        placeHolder.createNewUser(PlaceHolderTask.getNewTestUserJSon());

//      2.
        System.out.println("TASK 1.2============================================================================");
        HashMap<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "Ievhen Nesterenko");
        updateUser.put("username", "CezarY");

        placeHolder.updateNewUser(4, updateUser);

//      3.
        System.out.println("TASK 1.3============================================================================");
        placeHolder.deleteUser(testId);

//      4.
        System.out.println("TASK 1.4.1==========================================================================");
        HashMap<String, String> parameters = new HashMap<>();
        placeHolder.getUserInfo(parameters);

        System.out.println("TASK 1.4.2==========================================================================");
        HashMap<String, String> parametersId = new HashMap<>();
        parametersId.put("id", String.valueOf(testId));
        placeHolder.getUserInfo(parametersId);

        System.out.println("TASK 1.4.3==========================================================================");
        HashMap<String, String> parametersUserName = new HashMap<>();
        parametersUserName.put("username", testUserName);
        placeHolder.getUserInfo(parametersUserName);

//      TASK 2
        System.out.println("TASK 2==============================================================================");
        placeHolder.saveCommentsLastPost(testId);

//      TASK 3
        System.out.println("TASK 3==============================================================================");
        placeHolder.getNonCompletedToDo(testId);
    }

    public static String getNewTestUserJSon() {
        return "" + "{\n" + "    \"name\": \"Ievhen Nesterenko\",\n" + "    \"username\": \"CezarY\",\n" + "    \"email\": \"ie@ua.gh\",\n" + "    \"address\": {\n" + "      \"street\": \"Kibalch\",\n" + "      \"suite\": \"Apt. 007\",\n" + "      \"city\": \"Kharkiv\",\n" + "      \"zipcode\": \"61070\",\n" + "      \"geo\": {\n" + "        \"lat\": \"39.3159\",\n" + "        \"lng\": \"49.1496\"\n" + "      }\n" + "    },\n" + "    \"phone\": \"0-000-000-0000\",\n" + "    \"website\": \"web.org\",\n" + "    \"company\": {\n" + "      \"name\": \"My-Company\",\n" + "      \"catchPhrase\": \"GO-GO\",\n" + "      \"bs\": \"Home-home\"\n" + "    }\n" + "  }";
    }
}
