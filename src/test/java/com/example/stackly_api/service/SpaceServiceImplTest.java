package com.example.stackly_api.service;

import com.example.stackly_api.dto.SpaceRequest;
import com.example.stackly_api.exception.SpaceNameConflictException;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.repository.SpaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpaceServiceImplTest {

    @Nested
    @DisplayName("Space Tests")
    @ExtendWith(MockitoExtension.class)
    class SpaceTests {
        @InjectMocks
        private SpaceServiceImpl spaceService;
        @Mock
        private SpaceRepository spaceRepository;

        @Test
        @DisplayName("Throws IllegalArgumentException when spaceRequest is null and verifies error message")
        void createSpaceNullError() {
            SpaceRequest spaceReq = null;
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> spaceService.createSpace(spaceReq));
            assertEquals("Space request cannot be null.", thrown.getMessage());
        }

        @Test
        @DisplayName("Throws IllegalArgumentException when name is blank and verifies error message")
        void createSpaceBlankNameError() {
            SpaceRequest spaceReq = new SpaceRequest("");
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> spaceService.createSpace(spaceReq));
            assertEquals("Space name cannot be null or blank.", thrown.getMessage());
        }

        @Test
        @DisplayName("Throws SpaceNameConflictException when Space has already been created and " +
                "verifies error message")
        void createSpaceNameAlreadyCreated() {
            String conflictingName = "HR";
            SpaceRequest spaceReq = new SpaceRequest(conflictingName);

            when(spaceRepository.existsById(conflictingName)).thenReturn(true);

            SpaceNameConflictException thrown = assertThrows(SpaceNameConflictException.class,
                    () -> spaceService.createSpace(spaceReq));
            String expectedMessage = "Space already found with name: " + conflictingName;
            assertEquals(expectedMessage, thrown.getMessage());

            //Proves that code only runs once and
            //if name is conflicting, the code for saving never runs
            verify(spaceRepository, times(1)).existsById(conflictingName);
            verify(spaceRepository, never()).save(any());
        }

        @Test
        @DisplayName("Space is successfully created")
        void createSpace() {
            String spaceName = "Accounting";
            SpaceRequest spaceReq = new SpaceRequest(spaceName);
            Space expectedSpace = new Space(spaceName);

            when(spaceRepository.existsById(spaceName)).thenReturn(false);
            when(spaceRepository.save(any())).thenReturn(expectedSpace);

            Space actualSpace = spaceService.createSpace(spaceReq);

            assertEquals(expectedSpace.getSpaceName(), actualSpace.getSpaceName());

            //Proves that code only runs once and the Space is saved
            verify(spaceRepository, times(1)).existsById(spaceName);
            verify(spaceRepository, times(1)).save(any());

        }

    }

}