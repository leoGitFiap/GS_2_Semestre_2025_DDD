package service;

// este import só funciona se você adicionou a biblioteca json
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Classe que chama a API do Google Gemini (Plano 1).
 */
public class GeminiApiService {

    // 1. GERE SUA CHAVE NO GOOGLE AI STUDIO E COLE AQUI
    private static final String API_KEY = "COLE_SUA_CHAVE_API_AQUI"; // Sua chave vai aqui

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    public static String perguntarAoGemini(String area, String especialidade, String nicho) {

        // Validação da Chave
        if (API_KEY.equals("COLE_SUA_CHAVE_API_AQUI")) {
            return "ERRO: A CHAVE DA API não foi configurada. \n" +
                    "Por favor, gere uma chave no Google AI Studio e \n" +
                    "cole no arquivo 'GeminiApiService.java' na linha 19.";
        }

        try {
            // 1. Monta o "Prompt" (a pergunta) para a IA
            String prompt = String.format(
                    "Quais são as 3 principais habilidades técnicas (hard skills) " +
                            "e 2 cursos online recomendados para alguém que quer começar " +
                            "na carreira de '%s > %s > %s'? " +
                            "Seja breve, direto ao ponto e formate como uma lista.",
                    area, especialidade, nicho
            );

            // 2. Monta o corpo da requisição em JSON
            JSONObject parts = new JSONObject();
            parts.put("text", prompt);

            JSONObject contents = new JSONObject();
            contents.put("parts", new JSONObject[]{parts});

            JSONObject body = new JSONObject();
            body.put("contents", new JSONObject[]{contents});
            String jsonBody = body.toString();

            // 3. Cria o cliente e a requisição (Java 11+)
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // 4. Envia a requisição e espera a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 5. Analisa a resposta
            if (response.statusCode() == 200) {
                return parseiaResposta(response.body());
            } else {
                return "Erro: " + response.statusCode() + "\n" + parseiaErro(response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao conectar com a API: " + e.getMessage();
        }
    }

    private static String parseiaResposta(String jsonResponse) {
        try {
            JSONObject responseObj = new JSONObject(jsonResponse);
            return responseObj.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        } catch (Exception e) {
            return "Erro ao ler a resposta da IA. JSON: " + jsonResponse;
        }
    }

    private static String parseiaErro(String jsonResponse) {
        try {
            JSONObject responseObj = new JSONObject(jsonResponse);
            return responseObj.getJSONObject("error").getString("message");
        } catch (Exception e) {
            return jsonResponse;
        }
    }
}