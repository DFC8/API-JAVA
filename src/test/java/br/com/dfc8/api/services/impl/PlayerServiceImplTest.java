package br.com.dfc8.api.services.impl;

import br.com.dfc8.api.domain.Player;
import br.com.dfc8.api.domain.dto.PlayerDTO;
import br.com.dfc8.api.repositories.PlayerRepository;
import br.com.dfc8.api.services.exceptions.DataIntegratyVaiolationException;
import br.com.dfc8.api.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlayerServiceImplTest {

    public static final Integer ID = 1;
    public static final String NAME = "Valdir";
    public static final String EMAIL = "valdir@gmail.com";
    public static final String PASSWORD = "123";
    public static final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado";
    public static final Integer INDEX = 0;
    public static final String E_MAIL_JA_CADASTRADO_NO_SISTEMA = "E-mail já cadastrado no sistema";
    @InjectMocks
    private PlayerServiceImpl service;

    @Mock
    private PlayerRepository repository;

    @Mock
    private ModelMapper mapper;
    private Player player;
    private PlayerDTO playerDTO;
    private Optional<Player> optionalPlayer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startPlayer();
    }

    @Test
    void whenFindByIdThenReturnAnPlayerInstance() {
        when(repository.findById(anyInt())).thenReturn(optionalPlayer);

        Player response = service.findById(ID);

        assertNotNull(response);
        assertEquals(Player.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
    }

    @Test
    void whenFinByIdThenReturnAnObjectNotFoundException() {
        when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO));

        try {
            service.findById(ID);
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(OBJETO_NAO_ENCONTRADO, ex.getMessage());
        }
    }

    @Test
    void whenFindAllThenReturnAnListOfPlayer() {
        when(repository.findAll()).thenReturn(List.of(player));

        List<Player> response = service.findAll();

        assertNotNull(response);
        assertEquals(1,response.size());
        assertEquals(Player.class, response.get(INDEX).getClass());

        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(NAME, response.get(INDEX).getName());
        assertEquals(EMAIL, response.get(INDEX).getEmail());
        assertEquals(PASSWORD, response.get(INDEX).getPassword());
    }

    @Test
    void whenCreateThenReturnAnDateIntegrityViolationException() {
        when(repository.save(any())).thenReturn(player);

        Player response = service.create(playerDTO);

        assertNotNull(response);
        assertEquals(Player.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(NAME, response.getName());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void whenCreateThenReturnAnDataIntegrityViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalPlayer);

        try{
            optionalPlayer.get().setId(2);
            service.create(playerDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegratyVaiolationException.class, ex.getClass());
            assertEquals(E_MAIL_JA_CADASTRADO_NO_SISTEMA, ex.getMessage());
        }
    }
    @Test
    void whenUpdateThenReturnAnDateIntegrityViolationException() {
        when(repository.save(any())).thenReturn(player);

        Player response = service.update(playerDTO);

        assertNotNull(response);
        assertEquals(Player.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(NAME, response.getName());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void whenUpdateThenReturnAnDataIntegrityViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalPlayer);

        try{
            optionalPlayer.get().setId(2);
            service.create(playerDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegratyVaiolationException.class, ex.getClass());
            assertEquals(E_MAIL_JA_CADASTRADO_NO_SISTEMA, ex.getMessage());
        }
    }
    @Test
    void deleteWithSuccess() {
        when(repository.findById(anyInt())).thenReturn(optionalPlayer);
        doNothing().when(repository).deleteById(anyInt());
        service.delete(ID);
        verify(repository, times(1)).deleteById(anyInt());
    }

    @Test
    void whenDeleteThenReturnObjectNotFoundException() {
        when(repository.findById(anyInt()))
                .thenThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO));
        try {
            service.delete(ID);
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(OBJETO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
    private void startPlayer() {
        player = new Player(ID, NAME, EMAIL, PASSWORD);
        playerDTO = new PlayerDTO(ID, NAME, EMAIL, PASSWORD);
        optionalPlayer = Optional.of(new Player(ID, NAME, EMAIL, PASSWORD));
    }
}