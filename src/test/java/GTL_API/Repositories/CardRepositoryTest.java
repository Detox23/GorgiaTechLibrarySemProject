package GTL_API.Repositories;

import GTL_API.Exceptions.NotFoundException;
import GTL_API.Exceptions.UnknownException;
import GTL_API.MainApplicationClass;
import GTL_API.Models.Entities.CardEntity;
import GTL_API.Models.ReturnModels.CardReturn;
import GTL_API.Repositories.CardRepository.CardRepository;
import GTL_API.Repositories.CardRepository.ICardRepository;
import GTL_API.Repositories.CardRepository.ICardRepositoryCustom;
import GTL_API.TestDataSourceConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MainApplicationClass.class, TestDataSourceConfig.class})
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ICardRepository iCardRepository;

    @InjectMocks
    private CardRepository cardRepositoryInjected;

    @Mock
    private ICardRepository iCardRepositoryMock;

    @Mock
    private ModelMapper modelMapperMock;


    @Test
    public void findCardByNumber_returnsCardReturn(){
        CardReturn found = cardRepository.findCardByNumber(1025435856);
        Assert.assertEquals("2020-08-29", found.getExpirationDate().toString());
    }

    @Test(expected = NotFoundException.class)
    public void findCardByNumber_shouldThrowNotFoundException(){
        cardRepository.findCardByNumber(-5);
    }

    @Test
    public void findCardByNumber_throwsUnknownErrorWhenSomethingUnexpectedHappens(){
        try{
            MockitoAnnotations.initMocks(this);
            CardEntity cardEntity = new CardEntity();
            cardEntity.setNumber(1025435856);
            Mockito.when(modelMapperMock.map(Mockito.any(), Mockito.any())).thenThrow(new RuntimeException());
            Mockito.when(iCardRepositoryMock.findByNumberIs(Mockito.anyInt())).thenReturn(Optional.of(cardEntity));
            cardRepositoryInjected.findCardByNumber(1025435856);
        }catch(UnknownException unknownException){
            Assert.assertEquals("Unknown error while finding card with number: 1025435856", unknownException.getMessage());
        }
    }

    @Test
    public void createCard_shouldAddRecordToADatabase(){
        cardRepository.createCard(getCardEntity());
        Assert.assertEquals("231334-adasd", cardRepository.findCardByNumber(15).getPicture());
        iCardRepository.deleteById(15);
    }

    @Test
    public void createCardAsNull_throwsUnknownError(){
        try{
            cardRepository.createCard(null);
        }catch (UnknownException unknownException){
            Assert.assertEquals("Unknown error while creating a new card.", unknownException.getMessage());
        }
    }

    @Test
    public void createCard_throwsUnknownErrorWhenSomethingUnexpectedHappensAndDoesNotAddToADatabase(){
        try{
            MockitoAnnotations.initMocks(this);
            Mockito.when(iCardRepositoryMock.save(Mockito.any())).thenThrow(new RuntimeException());
            cardRepositoryInjected.createCard(getCardEntity());
        }catch (UnknownException unknownException){
            Assert.assertEquals("Unknown error while creating a new card.", unknownException.getMessage());
        }
    }

    @Test
    public void checkIfExistsByCardNumber_returnsTrueIfExists(){
        Assert.assertTrue(cardRepository.checkIfExistsByCardNumber(1025435856));
    }

    @Test
    public void checkIfExistsByCardNumber_returnsFalseIfDoesNotExist(){
        Assert.assertFalse(cardRepository.checkIfExistsByCardNumber(-5));
    }

    @Test
    public void checkIfExistsByCardNumber_throwsUnknownErrorWhenSomethingUnexpectedHappens(){
        try{
            MockitoAnnotations.initMocks(this);
            Mockito.when(iCardRepositoryMock.existsByNumber(Mockito.anyInt())).thenThrow(new RuntimeException());
            cardRepositoryInjected.checkIfExistsByCardNumber(getCardEntity().getNumber());
        }catch (UnknownException unknownException){
            Assert.assertEquals("Unknown error while determining an existence of a card with number 15.",
                    unknownException.getMessage());
        }
    }

    @Test
    public void deleteCard_setItsIsDeletedAttributeToFalse(){
        cardRepository.deleteCard(1025435856);
        Assert.assertTrue(iCardRepository.findById(1025435856).get().getDeleted());
        CardEntity toRevert = iCardRepository.findById(1025435856).get();
        toRevert.setDeleted(false);
        iCardRepository.save(toRevert);
    }

    @Test
    public void deleteCard_throwsUnknownErrorWhenSomethingUnexpectedHappens(){
        try{
            MockitoAnnotations.initMocks(this);
            Mockito.when(iCardRepositoryMock.save(Mockito.any())).thenThrow(new RuntimeException());
            cardRepositoryInjected.deleteCard(15);
        }catch (UnknownException unknownException){
            Assert.assertEquals("Unknown error while deleting a card with number: 15.",
                    unknownException.getMessage());
        }
    }


    private CardEntity getCardEntity(){
        CardEntity cardEntity = new CardEntity();
        cardEntity.setExpirationDate(new Date(System.currentTimeMillis()));
        cardEntity.setNumber(15);
        cardEntity.setLibraryEmployeeId(25);
        cardEntity.setPicture("231334-adasd");
        return cardEntity;
    }
}
