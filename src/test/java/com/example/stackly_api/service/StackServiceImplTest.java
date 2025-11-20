package com.example.stackly_api.service;

import com.example.stackly_api.dto.StackRequest;
import com.example.stackly_api.exception.SpaceNotFoundException;
import com.example.stackly_api.exception.StackNameConflictException;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.model.Stack;
import com.example.stackly_api.repository.SpaceRepository;
import com.example.stackly_api.repository.StackRepository;
import org.junit.jupiter.api.*;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StackServiceImplTest {

    @Nested
    @DisplayName("Stack Tests")
    //Below suppresses error about nesting even when working
    @SuppressWarnings("JUnitMalformedDeclaration")
    public static class StackTests {
        private StackServiceImpl stackService;
        private SpaceRepository spaceRepository;
        private StackRepository stackRepository;

        @BeforeEach
        //Set up this way due to needing Stream<Arguments>
        void setUp() {
            spaceRepository = Mockito.mock(SpaceRepository.class);
            stackRepository = Mockito.mock(StackRepository.class);

            stackService = new StackServiceImpl(spaceRepository, stackRepository);
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when stackRequest is null and verifies error message")
        void createStackNullError() {
            StackRequest stackRequest = null;
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> stackService.createStack(stackRequest));
            assertEquals("Stack request cannot be null.", thrown.getMessage());
        }

        private static Stream<Arguments> invalidFieldRequests() {

            return Stream.of(
                    Arguments.of(
                            new StackRequest("", "StackName", new HashMap<>(Map.of("Field1", "String"))),
                            "Space_Name_Blank"
                    ),
                    Arguments.of(
                            new StackRequest(null, "StackName", new HashMap<>(Map.of("Field1", "String"))),
                            "Space_Name_Null"
                    ),
                    Arguments.of(
                            new StackRequest("SpaceName", "", new HashMap<>(Map.of("Field1", "String"))),
                            "Stack_Name_Blank"
                    ),
                    Arguments.of(
                            new StackRequest("SpaceName", "StackName", null),
                            "Field_Schema_Null"
                    ),
                    Arguments.of(
                            new StackRequest("SpaceName", "StackName", new HashMap<>()),
                            "Field_Schema_Empty"
                    )
            );
        }

        @ParameterizedTest(name = "Fails when {1}")
        @MethodSource("invalidFieldRequests")
        @DisplayName("Verifies that fields throw IllegalArgumentException when null/blank/empty")
        void createStackInvalidFieldError(StackRequest invalidRequest, String testName) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> stackService.createStack(invalidRequest));
            assertEquals("Space/Stack/FieldSchema cannot be blank.", thrown.getMessage());

            //Proves that since field is blank or null,
            //the program stops and doesn't run any other code
            verify(stackRepository, never()).existsById(anyString());
            verify(stackRepository, never()).save(any());
            verify(spaceRepository, never()).findById(anyString());
        }

        @Test
        @DisplayName("Throws StackNameConflictException when Stack has already been created and " +
                "verifies error message")
        void createStackNameAlreadyCreated() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("key", "value");
            String conflictingName = "Employees";
            StackRequest stackReq = new StackRequest("HR", conflictingName, map);

            when(stackRepository.existsById(conflictingName)).thenReturn(true);

            StackNameConflictException thrown = assertThrows(StackNameConflictException.class,
                    () -> stackService.createStack(stackReq));
            String expectedMessage = "Stack already found with name: " + conflictingName;
            assertEquals(expectedMessage, thrown.getMessage());

            //Proves that code only runs once and
            //if name is conflicting, the code for saving never runs
            verify(stackRepository, times(1)).existsById(conflictingName);
            verify(spaceRepository, never()).findById(anyString());
            verify(stackRepository, never()).save(any());
        }

        @Test
        @DisplayName("Throws SpaceNotFoundException when parent Space does not exist")
        void createStackSpaceNotFound() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("key", "value");
            StackRequest stackReq = new StackRequest("HR", "Employees", map);
            String spaceName = stackReq.getSpaceName();
            Space mockSpace = new Space(spaceName);

            //This forces stack name to not show as conflicting
            when(stackRepository.existsById(anyString())).thenReturn(false);
            when(spaceRepository.findById(anyString())).thenReturn(Optional.empty());

            assertThrows(SpaceNotFoundException.class,
                    () -> stackService.createStack(stackReq));

            //Proves that no further code is run if there is an error
            verify(stackRepository, never()).save(any());
            //Proves findBy used correct name
            verify(spaceRepository, times(1)).findById(spaceName);
        }

        @Test
        @DisplayName("Stack is successfully created")
        void createStack() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("key", "value");
            String stackName = "Employees";
            StackRequest stackReq = new StackRequest("HR", stackName, map);
            String spaceName = "HR";
            Space mockSpace = new Space(spaceName);

            when(spaceRepository.findById(spaceName)).thenReturn(Optional.of(mockSpace));
            when(stackRepository.existsById(stackName)).thenReturn(false);
            Stack expectedStack = new Stack(mockSpace, stackName, map);
            when(stackRepository.save(any())).thenReturn(expectedStack);

            Stack actualStack = stackService.createStack(stackReq);

            assertEquals(expectedStack.getStackName(), actualStack.getStackName());

            //Proves that code only runs once and the Space is saved
            verify(stackRepository, times(1)).existsById(stackName);
            verify(stackRepository, times(1)).save(any());

        }

    }

}