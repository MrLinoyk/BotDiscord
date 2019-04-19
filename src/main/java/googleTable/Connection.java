package googleTable;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import events.EventRead;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Connection extends ListenerAdapter {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/clientSecret.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Connection.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    public static void read() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1x1rD4a2TdKG2X-qGUsUYHDVVXRrayXMcyr5nRmn6Fj8";
        final String range = "A2:C";
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("Name, Major");
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                System.out.printf("%s, %s\n", row.get(0), row.get(1), row.get(2));
            }
        }

    }

    public static void writeData() throws IOException, GeneralSecurityException {

        final String spreadsheetId = "1x1rD4a2TdKG2X-qGUsUYHDVVXRrayXMcyr5nRmn6Fj8";
        int forRange = 2;
        String range = "A" + forRange + ":H";
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {

            List<List<Object>> val = Arrays.asList
                    (
                            Arrays.asList(
                                    EventRead.getMessage()
                            )
                            // Additional rows ...
//                Arrays.asList(
//                        EventRead.getMessage()
//                  )
                    );
//
//        ValueRange body = new ValueRange().setValues(val);
//        UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId,range,body)
//                .setValueInputOption("RAW").execute();
//        System.out.printf("%d cells updated.", result.getUpdatedCells());


            List<ValueRange> data = new ArrayList<>();
            data.add(new ValueRange()
                    .setRange(range)
                    .setValues(val));
            //     Additional ranges to update ...

            BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
                    .setValueInputOption("RAW")
                    .setData(data);
            BatchUpdateValuesResponse result =
                    service.spreadsheets().values().batchUpdate(spreadsheetId, body).execute();
            System.out.printf("%d cells updated.", result.getTotalUpdatedCells());
        } else {
            forRange++;

            List<List<Object>> val = Arrays.asList
                    (
                            Arrays.asList(
                                    EventRead.getMessage()
                            )
                            // Additional rows ...
//                Arrays.asList(
//                        EventRead.getMessage()
//                  )
                    );
//
//        ValueRange body = new ValueRange().setValues(val);
//        UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId,range,body)
//                .setValueInputOption("RAW").execute();
//        System.out.printf("%d cells updated.", result.getUpdatedCells());


            List<ValueRange> data = new ArrayList<>();
            data.add(new ValueRange()
                    .setRange(range)
                    .setValues(val));
            //     Additional ranges to update ...

            BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
                    .setValueInputOption("RAW")
                    .setData(data);
            BatchUpdateValuesResponse result =
                    service.spreadsheets().values().batchUpdate(spreadsheetId, body).execute();
            System.out.printf("%d cells updated.", result.getTotalUpdatedCells());

        }
    }
}