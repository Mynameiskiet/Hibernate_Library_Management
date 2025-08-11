package com.tuankiet.utils;

import com.tuankiet.dto.request.CreateAuthorRequest;
import com.tuankiet.dto.request.CreateBookRequest;
import com.tuankiet.dto.request.CreateBorrowingRequest;
import com.tuankiet.dto.request.CreateMemberRequest;
import com.tuankiet.dto.request.UpdateAuthorRequest;
import com.tuankiet.dto.request.UpdateBookRequest;
import com.tuankiet.dto.request.UpdateBorrowingRequest;
import com.tuankiet.dto.request.UpdateMemberRequest;
import com.tuankiet.dto.response.AuthorResponse;
import com.tuankiet.dto.response.BookResponse;
import com.tuankiet.dto.response.BorrowingResponse;
import com.tuankiet.dto.response.MemberResponse;
import com.tuankiet.entities.Author;
import com.tuankiet.entities.Book;
import com.tuankiet.entities.Borrowing;
import com.tuankiet.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Utility class for mapping properties between DTOs and entities.
 * Uses reflection for generic mapping.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class MapperUtil {

    private static final Logger logger = LoggerFactory.getLogger(MapperUtil.class);

    /**
     * Maps properties from a source object to a new instance of the target class.
     * Only properties with matching names and types are mapped.
     * 
     * @param <S> The type of the source object.
     * @param <T> The type of the target object.
     * @param source The object to map from.
     * @param targetClass The class of the target object.
     * @return A new instance of the target class with mapped properties.
     */
    public <S, T> T map(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            map(source, target);
            return target;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            logger.error("Error creating instance of target class {}: {}", targetClass.getName(), e.getMessage(), e);
            throw new RuntimeException("Failed to create instance of target class: " + targetClass.getName(), e);
        }
    }

    /**
     * Maps properties from a source object to an existing target object.
     * Only properties with matching names and types are mapped. Null values in source are ignored.
     * 
     * @param <S> The type of the source object.
     * @param <T> The type of the target object.
     * @param source The object to map from.
     * @param target The existing object to map to.
     */
    public <S, T> void map(S source, T target) {
        if (source == null || target == null) {
            return;
        }

        for (Method setter : target.getClass().getMethods()) {
            if (setter.getName().startsWith("set") && setter.getParameterTypes().length == 1) {
                String propertyName = setter.getName().substring(3); // e.g., "Name" from "setName"
                try {
                    Method getter = source.getClass().getMethod("get" + propertyName);
                    Object value = getter.invoke(source);

                    // Only map if the value is not null and types are compatible
                    if (value != null && setter.getParameterTypes()[0].isAssignableFrom(getter.getReturnType())) {
                        setter.invoke(target, value);
                    }
                } catch (NoSuchMethodException e) {
                    // Getter not found in source, ignore this property
                    logger.trace("No getter for property {} in source class {}", propertyName, source.getClass().getSimpleName());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.warn("Error mapping property {} from {} to {}: {}", propertyName, source.getClass().getSimpleName(), target.getClass().getSimpleName(), e.getMessage());
                }
            }
        }
        logger.debug("Mapped properties from {} to {}", source.getClass().getSimpleName(), target.getClass().getSimpleName());
    }

    /**
     * Extracts the ID from an update request DTO.
     * This method uses a series of instanceof checks to determine the DTO type
     * and call the appropriate getId() method.
     * 
     * @param updateRequest The update request DTO.
     * @return The UUID ID from the DTO.
     * @throws IllegalArgumentException if the DTO type is not recognized or does not have an ID.
     */
    public UUID getIdFromUpdateRequest(Object updateRequest) {
        if (updateRequest instanceof UpdateAuthorRequest) {
            return ((UpdateAuthorRequest) updateRequest).getId();
        } else if (updateRequest instanceof UpdateBookRequest) {
            return ((UpdateBookRequest) updateRequest).getId();
        } else if (updateRequest instanceof UpdateMemberRequest) {
            return ((UpdateMemberRequest) updateRequest).getId();
        } else if (updateRequest instanceof UpdateBorrowingRequest) {
            return ((UpdateBorrowingRequest) updateRequest).getId();
        } else {
            throw new IllegalArgumentException("Unsupported update request DTO type: " + updateRequest.getClass().getName());
        }
    }
}
