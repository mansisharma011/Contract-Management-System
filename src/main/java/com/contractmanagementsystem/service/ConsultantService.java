package com.contractmanagementsystem.service;

import com.contractmanagementsystem.dto.ContractResponseDTO;
import com.contractmanagementsystem.dto.QuestionAnswerResponseDTO;
import com.contractmanagementsystem.exception.ContractException;
import com.contractmanagementsystem.model.Contract;
import com.contractmanagementsystem.model.ContractStatus;
import com.contractmanagementsystem.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ConsultantService {

    private final ContractRepository contractRepository;


    public ResponseEntity<Map<String,Object>>getContract(String id){
        Contract contract=contractRepository.findById(id).orElseThrow(() -> new ContractException("No Such Contract Exist"));
        ContractResponseDTO responseDTO=new ContractResponseDTO();
        responseDTO.setId(contract.getId());
        responseDTO.setContractName(contract.getContractName());
        responseDTO.setStatus(contract.getStatus());
        Map<String,Object> response=new HashMap<>();
        response.put("message","Contract details Successfully fetched");
        response.put("Contract Details",responseDTO);
        return ResponseEntity.ok().body(response);
    }


    public ResponseEntity<Map<String, Object>>getAllContracts() {

        List<Contract> contracts = contractRepository.findAll();

        Map<String, Object> response = new HashMap<>();

        response.put("message",contracts.isEmpty() ? "No contracts found" : "Contracts fetched successfully");
        List<ContractResponseDTO> responseDTOList=contracts.stream()
                .map(contract -> {
                    ContractResponseDTO dto = new ContractResponseDTO();

                    dto.setId(contract.getId());

                    dto.setContractName(contract.getContractName());

                    dto.setStatus(contract.getStatus());
                    return dto;
                })
                .toList();

        response.put("data",responseDTOList);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String,String>> draftToReview(String id){

        Contract contract=contractRepository.findById(id).orElseThrow(() -> new ContractException("No Contract With This ID exist"));
        if(contract.getStatus() == ContractStatus.DRAFT){
            contract.setStatus(ContractStatus.REVIEW);
            contractRepository.save(contract);
            Map<String,String> response=new HashMap<>();
            response.put("message","Status Successfully updated to Review");
            return ResponseEntity.ok().body(response);
        }
        throw new ContractException("Contract can't be updated as the current status is not draft");

    }

    public ResponseEntity<Map<String,String>> reviewToApproved(String id){

        Contract contract=contractRepository.findById(id).orElseThrow(() -> new ContractException("No Contract With This ID exist"));
        if(contract.getStatus() == ContractStatus.REVIEW){
            contract.setStatus(ContractStatus.APPROVED);
            contractRepository.save(contract);
            Map<String,String> response=new HashMap<>();
            response.put("message","Status Successfully updated to Approved");
            return ResponseEntity.ok().body(response);
        }
        throw new ContractException("Contract can't be updated as the current status is not Review");

    }

    public QuestionAnswerResponseDTO askQuestion(String id, String question) {

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractException("No Contract With This ID exist"));

        if (question == null || question.isBlank()) {
            throw new ContractException("Question cannot be empty");
        }

        String extractedText = contract.getExtractedText();

        if (extractedText == null || extractedText.isBlank()) {
            throw new ContractException("No extracted text found for this contract");
        }

        Set<String> questionTokens = tokenize(question);

        List<String> chunks = splitIntoChunks(extractedText);

        List<ScoredParagraph> matches = chunks.stream()
                .map(chunk -> scoreChunk(chunk, questionTokens))
                .filter(result -> result.score() > 0)
                .sorted((a, b) -> Integer.compare(b.score(), a.score()))
                .limit(3)
                .toList();

        if (matches.isEmpty()) {
            return new QuestionAnswerResponseDTO(
                    contract.getId(),
                    contract.getContractName(),
                    question,
                    "Sorry, I could not find relevant information in this contract.",
                    0
            );
        }

        String answer = matches.stream()
                .map(ScoredParagraph::paragraph)
                .distinct()
                .reduce((a, b) -> a + "\n\n" + b)
                .orElse("");

        int totalScore = matches.stream()
                .mapToInt(ScoredParagraph::score)
                .sum();

        return new QuestionAnswerResponseDTO(
                contract.getId(),
                contract.getContractName(),
                question,
                answer,
                totalScore
        );
    }

    private Set<String> tokenize(String text) {

        Set<String> stopWords = Set.of(
                "what", "which", "when", "where", "why", "how",
                "is", "are", "was", "were", "the", "a", "an",
                "of", "in", "on", "at", "to", "for", "from",
                "by", "with", "and", "or", "does", "do", "did",
                "can", "could", "should", "tell", "me", "about",
                "this", "that", "contract", "agreement", "please"
        );

        return Arrays.stream(text.toLowerCase()
                        .replaceAll("[^a-z0-9\\s]", " ")
                        .split("\\s+"))
                .map(String::trim)
                .filter(word -> word.length() > 2)
                .map(this::normalizeWord)
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.toSet());
    }

    private String normalizeWord(String word) {

        if (word.endsWith("ies") && word.length() > 3) {
            return word.substring(0, word.length() - 3) + "y";
        }

        if (word.endsWith("s") && word.length() > 3) {
            return word.substring(0, word.length() - 1);
        }

        return word;
    }

    private List<String> splitIntoChunks(String text) {

        String cleanedText = text
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replaceAll("\\s+", " ")
                .trim();

        List<String> chunks = new ArrayList<>();

        String[] sections = cleanedText.split("(?=\\b\\d+\\.\\s+[A-Z])");

        for (String section : sections) {
            String chunk = section.trim();

            if (chunk.length() > 20) {
                chunks.add(chunk);
            }
        }

        if (!chunks.isEmpty()) {
            return chunks;
        }

        return List.of(cleanedText);
    }

    private ScoredParagraph scoreChunk(String chunk, Set<String> questionTokens) {

        Set<String> chunkTokens = tokenize(chunk);

        int score = 0;

        for (String token : questionTokens) {
            if (chunkTokens.contains(token)) {
                score++;
            }
        }

        return new ScoredParagraph(chunk, score);
    }
    private record ScoredParagraph(String paragraph, int score) {
    }


}
