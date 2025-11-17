package com.example.stackly_api.service;

import com.example.stackly_api.dto.StackRequest;
import com.example.stackly_api.exception.SpaceNotFoundException;
import com.example.stackly_api.exception.StackNameConflictException;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.repository.SpaceRepository;
import com.example.stackly_api.model.Stack;
import com.example.stackly_api.repository.StackRepository;
import org.springframework.stereotype.Service;

@Service
public class StackServiceImpl implements StackService{
    private final SpaceRepository spaceRepository;
    private final StackRepository stackRepository;

    public StackServiceImpl(SpaceRepository spaceRepository,StackRepository stackRepository) {
        this.spaceRepository = spaceRepository;
        this.stackRepository= stackRepository;
    }

    @Override
    public Stack createStack(StackRequest stackRequest) {
        if ((stackRequest.spaceName == null || stackRequest.spaceName.isBlank()) ||
                (stackRequest.stackName == null || stackRequest.stackName.isBlank()) ||
                (stackRequest.fieldSchema == null) || (stackRequest.fieldSchema.isEmpty())) {
            throw new IllegalArgumentException("Space/Stack/FieldSchema cannot be blank.");
        }

        if (stackRepository.existsById(stackRequest.stackName)) {
            throw new StackNameConflictException("Stack already found with name: "
                    + stackRequest.spaceName);
        }

        Space space = spaceRepository.findById(stackRequest.spaceName).
                orElseThrow(() -> new SpaceNotFoundException("Space not found with name: "
                        + stackRequest.spaceName));
        Stack stack = new Stack(space, stackRequest.stackName, stackRequest.fieldSchema);
        return stackRepository.save(stack);
    }
}
