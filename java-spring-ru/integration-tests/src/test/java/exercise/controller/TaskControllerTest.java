package exercise.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.InstanceOfAssertFactories.atomicStampedReference;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    @Test
    public void testShow() throws Exception {
        var task = createTask();
        taskRepository.save(task);

        var result = mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertThatJson(result.getResponse().getContentAsString())
                .and(
                        a -> a.node("id").isEqualTo(task.getId()),
                        a -> a.node("title").isEqualTo(task.getTitle()),
                        a -> a.node("description").isEqualTo(task.getDescription())
                );
    }

    @Test
    public void testCreate() throws Exception {
        var task = createTask();

        var result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andReturn();

        assertThatJson(result.getResponse().getContentAsString())
                .and(
                        a -> a.node("title").isEqualTo(task.getTitle()),
                        a -> a.node("description").isEqualTo(task.getDescription())
                );
    }

    @Test
    public void testUpdate() throws Exception {
        var task = createTask();
        taskRepository.save(task);

        var updTask = createTask();
        var result = mockMvc.perform(put("/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(updTask)))
                .andExpect(status().isOk())
                .andReturn();

        assertThatJson(result.getResponse().getContentAsString())
                .and(
                        a -> a.node("id").isEqualTo(task.getId()),
                        a -> a.node("title").isEqualTo(updTask.getTitle()),
                        a -> a.node("description").isEqualTo(updTask.getDescription())
                );
    }

    @Test
    public void testDelete() throws Exception {
        var task = createTask();
        taskRepository.save(task);

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk());
        var result = mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("Task with id " + task.getId() + " not found");
    }

    private Task createTask() {
        return Instancio.of(Task.class)
                        .ignore(Select.field(Task::getId))
                        .ignore(Select.field(Task::getCreatedAt))
                        .ignore(Select.field(Task::getUpdatedAt))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
    }
    // END
}
