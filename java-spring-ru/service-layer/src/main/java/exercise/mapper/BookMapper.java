package exercise.mapper;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.model.Book;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class BookMapper {

    // BEGIN
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "authorId", source = "author.id")
    public abstract BookDTO map(Book model);

    @Mapping(source = "authorId", target = "author")
    public abstract Book map(BookCreateDTO dto);
    // END

    @Mapping(target = "author", source = "authorId")
    public abstract void update(BookUpdateDTO dto, @MappingTarget Book model);
}
