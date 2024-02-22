import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.utils.URIBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class PlaceHolder {
    private static final String URL_API = "https://jsonplaceholder.typicode.com";
    private static final String RESOURCE_USERS = "users";
    private static final String RESOURCE_POSTS = "posts";
    private static final String RESOURCE_COMMENT = "comments";
    private static final String RESOURCE_TODOS = "todos";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    public void createNewUser(String userJSon) throws IOException, InterruptedException, URISyntaxException {

        URI uri = new URIBuilder(URL_API).setPathSegments(RESOURCE_USERS).build();

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(userJSon)).build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (responseIsOk(response)) {
            System.out.println("new user: " + response.body());
        } else {
            System.out.println("Error creating new user = " + response);
        }
    }


    public void updateNewUser(int userId, HashMap<String, String> upd) throws IOException, InterruptedException, URISyntaxException {

        String userJSon = GSON.toJson(upd);

        URI uri = new URIBuilder(URL_API).setPathSegments(RESOURCE_USERS, String.valueOf(userId)).build();

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(userJSon)).build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (responseIsOk(response)) {
            System.out.println("Update user: " + response.body());
        } else {
            System.out.println("Error update user = " + response);
        }

    }

    public void deleteUser(int UserId) throws IOException, InterruptedException, URISyntaxException {

        URI uri = new URIBuilder(URL_API).setPathSegments(RESOURCE_USERS, String.valueOf(UserId)).build();

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").DELETE().build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (responseIsOk(response)) {
            System.out.println("Delete user: " + UserId);
        } else {
            System.out.println("Error delete user = " + UserId + response);
        }
    }

    public void getUserInfo(HashMap<String, String> parameters) throws URISyntaxException, IOException, InterruptedException {

        URIBuilder uriBuilder = new URIBuilder(URL_API).setPathSegments(RESOURCE_USERS);
        if (parameters.containsKey("id")) {
            uriBuilder.setPathSegments(RESOURCE_USERS, parameters.get("id"));
        }
        if (parameters.containsKey("username")) {
            uriBuilder.addParameter("username", parameters.get("username"));
        }

        URI uri = uriBuilder.build();

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").GET().build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (responseIsOk(response)) {
            System.out.println("Get info user: " + response.body());
        } else {
            System.out.println("Error get user = " + response);
        }
    }

    public void saveCommentsLastPost(int userId) throws URISyntaxException, IOException, InterruptedException {
        URI uriGetPosts = new URIBuilder(URL_API).setPathSegments(RESOURCE_USERS, String.valueOf(userId), RESOURCE_POSTS).build();

        HttpRequest requestPosts = HttpRequest.newBuilder().uri(uriGetPosts).header("Content-Type", "application/json").GET().build();
        HttpResponse<String> responsePosts = CLIENT.send(requestPosts, HttpResponse.BodyHandlers.ofString());

        if (!responseIsOk(responsePosts)) {
            System.out.println("Error get info last posts " + userId);
            return;
        }

        Type listType = new TypeToken<List<HashMap<String, String>>>() {
        }.getType();
        List<HashMap<String, String>> responseList = new Gson().fromJson(responsePosts.body(), listType);

        Optional<HashMap<String, String>> optionalMap = responseList.stream().max(Comparator.comparingInt(i -> Integer.parseInt(i.get("id"))));
        if (optionalMap.isEmpty()) {
            System.out.println("Failed to determine last post");
            return;
        }
        String postId = optionalMap.get().get("id");

        URI uriGetComments = new URIBuilder(URL_API).setPathSegments(RESOURCE_POSTS, postId, RESOURCE_COMMENT).build();

        HttpRequest requestComments = HttpRequest.newBuilder().uri(uriGetComments).header("Content-Type", "application/json").GET().build();
        HttpResponse<String> responseComments = CLIENT.send(requestComments, HttpResponse.BodyHandlers.ofString());

        if (!responseIsOk(responseComments)) {
            System.out.println("Error get info comments last post " + userId);
            return;
        }

        String filePath = "user-" + userId + "-post-" + postId + "-comments.gson";

        FileWriter writer = new FileWriter(filePath);
        writer.write(responseComments.body());
        writer.close();

        System.out.println("Save file: " + filePath);
    }

    public void getNonCompletedToDo(int userId) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URIBuilder(URL_API).setPathSegments(RESOURCE_USERS, String.valueOf(userId), RESOURCE_TODOS).build();

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").GET().build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (!responseIsOk(response)) {
            System.out.println("Error get info todos for user id: " + userId);
            return;
        }

        Type listType = new TypeToken<List<HashMap<String, String>>>() {
        }.getType();
        List<HashMap<String, String>> responseList = new Gson().fromJson(response.body(), listType);

        List<HashMap<String, String>> completedToDo = responseList.stream().filter(i -> Boolean.parseBoolean(i.get("completed"))).peek((i) -> {
            i.remove("userId");
            i.remove("completed");
        }).collect(Collectors.toList());

        System.out.println("completedToDo = " + completedToDo);
    }

    private boolean responseIsOk(HttpResponse<String> response) {
        if (response.statusCode() >= 200 && response.statusCode() <= 299) return true;

        return false;
    }


}
