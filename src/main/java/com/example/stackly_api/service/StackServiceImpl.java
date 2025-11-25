package com.example.stackly_api.service;

import com.example.stackly_api.dto.StackRequest;
import com.example.stackly_api.exception.SpaceNotFoundException;
import com.example.stackly_api.exception.StackNameConflictException;
import com.example.stackly_api.model.Space;
import com.example.stackly_api.repository.SpaceRepository;
import com.example.stackly_api.model.Stack;
import com.example.stackly_api.repository.StackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (stackRequest == null) {
            throw new IllegalArgumentException("Stack request cannot be null.");
        }

        if (stackRequest.getSpaceName() == null ||
                stackRequest.getSpaceName().isBlank() ||

                stackRequest.getStackName() == null ||
                stackRequest.getStackName().isBlank() ||

                stackRequest.getFieldSchema() == null ||
                stackRequest.getFieldSchema().isEmpty()) {
            throw new IllegalArgumentException("Space/Stack/FieldSchema cannot be blank.");
        }

        if (stackRepository.findByStackName(stackRequest.getStackName()).isPresent()) {
            throw new StackNameConflictException("Stack already found with name: "
                    + stackRequest.getStackName());
        }

        Space space = spaceRepository.findById(stackRequest.getSpaceName()).
                orElseThrow(() -> new SpaceNotFoundException("Space not found with name: "
                        + stackRequest.getSpaceName()));
        Stack stack = new Stack(space, stackRequest.getStackName(), stackRequest.getFieldSchema());
        return stackRepository.save(stack);
    }

    @Override
    public List<Stack> getAllStacksPerSpace(String spaceName) {
        return stackRepository.findBySpace_spaceName(spaceName);
    }

    @Override
    public Optional<Stack> getStackByStackName(String stackName) {
        return stackRepository.findByStackName(stackName);
    }

    @Override
    public List<String> getAllStackNames() {
        return stackRepository.findAllStackNames();
    }
}
