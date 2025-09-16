package br.com.vitor.cybercrimes.tracker.core.integration.geminiai;

import br.com.vitor.cybercrimes.tracker.core.utils.FileUtils;
import com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Part;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Log4j2
@Component
public class GeminiAiIntegrationClient {

    private final Client client;

    public GeminiAiIntegrationClient(@Qualifier("geminiAiClient") Client client) {
        this.client = client;
    }

    public String classifyFileWithAi(File file) {
        log.info("Classifying file with AI: {}", file.getAbsolutePath());
        Part legalProcessPart = Part.fromBytes(FileUtils.getBytesFrom(file), "application/pdf");
        Part articlePart = Part.fromBytes(FileUtils.getBytesFrom("C:\\Users\\vitor\\dev\\projects\\cyber-crimes-tracker\\config\\CLASSIFICATION SYSTEM FOR CYBERCRIM.txt"), "text/plain");
        Part imagePart = Part.fromBytes(FileUtils.getBytesFrom("C:\\Users\\vitor\\dev\\projects\\cyber-crimes-tracker\\config\\tabela.png"), "image/png");

        Content content = Content.fromParts(
                articlePart, imagePart,
                Part.fromText("Classifique o processo legal em PDF:"),
                legalProcessPart);

        return client.models.generateContent("gemini-2.5-flash", content, GenerateContentConfig.builder()
                .systemInstruction(Content.fromParts(Part.fromText("Você é um expert em classificar casos judiciais se " +
                        " são um crime cibernético ou não. Além disso, você sabe classificar qual o tipo de crime " +
                        " cibernético ocorreu, levando em consideração o artigo em anexo, e principalmente a imagem da " +
                        " tabela em anexo, que também se encontra no artigo. a sua classificação deverá ser simples e direta. " +
                        " Você deve responder usando apenas 3 tópicos: \"Status\", \"Classificação\" e \"Justificativa\". " +
                        " No tópico \"Status\" você deverá colocar apenas \"Sim\" quando for um crime cibernético e \"Não\" " +
                        " quando não for um crime cibernético. No tópico \"Classificação\" você deverá colocar apenas a " +
                        " classificação, sem mais explicações, além disso deverá ter apenas uma classificação, se você " +
                        " achar que tem mais de uma classificação, então coloque apenas aquela que é a principal do caso. " +
                        " No tópico \"Justificativa\" você deverá dar uma breve explicação " +
                        " do caso, com no máximo 50 palavras.\n" +
                        "Exemplos:\n" +
                        "* Status: Sim\n" +
                        "* Classificação: B2\n" +
                        "* Justificativa: A fraude consistiu em uma pessoa se passar por gerente de banco no WhatsApp " +
                        " para induzir a vítima a fazer uma transferência bancária para a conta da ré."))).build()).text();
    }
}
