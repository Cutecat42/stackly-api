package com.example.stackly_api.service;

import com.example.stackly_api.dto.DocumentRequest;
import com.example.stackly_api.exception.StackNotFoundException;
import com.example.stackly_api.model.Document;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.model.Stack;
import com.example.stackly_api.repository.DocumentRepository;
import com.example.stackly_api.repository.StackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Nested
    @DisplayName("Document Tests")
    //Below suppresses error about nesting even when working
    @SuppressWarnings("JUnitMalformedDeclaration")
    public static class DocumentTests {
        private DocumentServiceImpl documentService;
        private DocumentRepository documentRepository;
        private StackRepository stackRepository;

        @BeforeEach
            //Set up this way due to needing Stream<Arguments>
        void setUp() {
            documentRepository = Mockito.mock(DocumentRepository.class);
            stackRepository = Mockito.mock(StackRepository.class);

            documentService = new DocumentServiceImpl(stackRepository, documentRepository);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when documentRequest is null " +
                "and verifies error message")
        void createDocumentNullError() {
            DocumentRequest documentReq = null;
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> documentService.createDocument(documentReq));
            assertEquals("Document request cannot be null.", thrown.getMessage());
        }

        private static Stream<Arguments> invalidFieldRequests() {

            return Stream.of(
                    Arguments.of(
                            new DocumentRequest("", new HashMap<>(Map.of("Field1", "String"))),
                            "Stack_Name_Blank"
                    ),
                    Arguments.of(
                            new DocumentRequest(null, new HashMap<>(Map.of("Field1", "String"))),
                            "Stack_Name_Null"
                    ),
                    Arguments.of(
                            new DocumentRequest("StackName", null),
                            "CustomData_Null"
                    ),
                    Arguments.of(
                            new DocumentRequest("StackName", new HashMap<>()),
                            "CustomData_Empty"
                    )
            );
        }

        @ParameterizedTest(name = "Fails when {1}")
        @MethodSource("invalidFieldRequests")
        @DisplayName("Verifies that fields throw IllegalArgumentException when null/blank/empty")
        void createStackInvalidFieldError(DocumentRequest invalidRequest, String testName) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> documentService.createDocument(invalidRequest));
            assertEquals("Stack/CustomRepository Data Fields cannot be blank.", thrown.getMessage());

            //Proves that since field is blank or null,
            //the program stops and doesn't run any other code
            verify(stackRepository, never()).findById(anyString());
            verify(stackRepository, never()).save(any());
        }

        @Test
        @DisplayName("Throws StackNotFoundException when parent Stack does not exist")
        void createDocumentStackNotFound() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("key", "value");
            DocumentRequest documentReq = new DocumentRequest("HR", map);
            String stackName = documentReq.getStackName();

            when(stackRepository.findById(anyString())).thenReturn(Optional.empty());

            assertThrows(StackNotFoundException.class,
                    () -> documentService.createDocument(documentReq));

            //Proves that no further code is run if there is an error
            verify(documentRepository, never()).save(any());
            //Proves findBy used correct name
            verify(stackRepository, times(1)).findById(stackName);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when data contains an invalid field")
        void createDocumentInvalidFieldInMap() {
            String stackName = "Employees";
            String invalidKey = "invalidKey";

            HashMap<String, Object> customData = new HashMap<>();
            customData.put("validKey", "Jane");
            customData.put(invalidKey, "2025-01-01");
            DocumentRequest documentReq = new DocumentRequest(stackName, customData);

            HashMap<String, Object> allowedSchema = new HashMap<>();
            allowedSchema.put("validKey", "Jane");

            Stack mockStack = new Stack(null, stackName, allowedSchema);

            when(stackRepository.findById(stackName)).thenReturn(Optional.of(mockStack));

            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> documentService.createDocument(documentReq));
            String expectedMessagePart = "Field '" + invalidKey + "' not allowed";
            assertTrue(thrown.getMessage().contains(expectedMessagePart));

            // Proves that no further code is run if there is an error
            verify(stackRepository, times(1)).findById(stackName);
            verify(documentRepository, never()).save(any());
        }

        @Test
        @DisplayName("Document is successfully created")
        void createDocument() {
            String stackName = "Employees";

            HashMap<String, Object> customData = new HashMap<>();
            customData.put("validKey", "Jane");

            HashMap<String, Object> allowedSchema = new HashMap<>();
            allowedSchema.put("validKey", "Jane");

            DocumentRequest documentReq = new DocumentRequest(stackName, customData);

            Space simpleSpace = new Space("HR");
            Stack mockStack = new Stack(simpleSpace, stackName, allowedSchema);

            //Document expectedDocument = new Document(mockStack, customData);

            when(stackRepository.findById(stackName)).thenReturn(Optional.of(mockStack));
            when(documentRepository.save(any(Document.class))).thenReturn(expectedDocument);

            Document actualDocument = documentService.createDocument(documentReq);

            assertNotNull(actualDocument, "The actual document should not be null.");
            assertEquals(expectedDocument.getCustomData(),
                    actualDocument.getCustomData(), "Custom data must match.");
            assertEquals(expectedDocument.getStackName(),
                    actualDocument.getStackName(), "Document must be linked to the correct Stack.");

            //Proves that code only runs once and the Space is saved
            verify(stackRepository, times(1)).findById(stackName);
            verify(documentRepository, times(1)).save(any(Document.class));
            verify(stackRepository, never()).save(any());

        }

    }

}