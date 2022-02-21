package com.stackroute.interest;

import com.stackroute.exception.UserAlreadyExistsException;
import com.stackroute.exception.UserNotFoundException;
import com.stackroute.model.Interest;
import com.stackroute.repository.InterestRepository;
import com.stackroute.service.InterestServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class InterestServiceTest {

    @Mock
    private InterestRepository interestRepository;

    @InjectMocks
    private InterestServiceImpl interestService;

    private Interest interest1;
    private Interest interest2;

    @BeforeEach
    public void setUp(){
        List<String> a = new ArrayList<>();
        a.add("India");
        a.add("Canada");
        List<String> b = new ArrayList<>();
        a.add("United States");
        a.add("Mexico");
        interest1 = new Interest("100", "test1",a);
        interest2 = new Interest("101", "test2",b);
    }

    @AfterEach
    public void tearDown(){
        interestRepository.deleteAll();
        interest1 = null;
        interest2 = null;
    }

    @Test
    public void saveInterestTest() throws UserAlreadyExistsException {
        when(interestRepository.save(any())).thenReturn(interest1);
        assertEquals(interest1, interestService.saveInterest(interest1));
        verify(interestRepository, times(1)).save(any());
        verify(interestRepository, times(1)).findInteresttById(any());
    }

    @Test
    public void getInterestTest() throws UserNotFoundException {
        when(interestRepository.findInteresttById(any())).thenReturn(interest2);
        assertEquals(interest2, interestService.getInterest("test2"));
        verify(interestRepository, times(2)).findInteresttById(any());
    }

    @Test
    public void updateInterestTest() throws UserNotFoundException{
        List<String> c = new ArrayList<>();
        c.add("China");
        c.add("Russia");
        Interest exp = new Interest("100", "test1",c);
        when(interestRepository.findInteresttById(any())).thenReturn(interest1);
        when(interestRepository.save(any())).thenReturn(exp);
        assertEquals(exp, interestService.updateInterest(exp));
        verify(interestRepository, times(1)).save(any());
    }
}
