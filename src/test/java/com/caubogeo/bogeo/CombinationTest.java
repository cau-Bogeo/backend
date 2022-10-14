package com.caubogeo.bogeo;

import com.caubogeo.bogeo.domain.medicine.AvoidCombination;
import com.caubogeo.bogeo.repository.CombinationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CombinationTest {
    @Autowired
    private CombinationRepository combinationRepository;

    @Test
    public void 중복데이터_값_넣어지는지_확인(){
        AvoidCombination avoidCombination = AvoidCombination.builder()
                .firstMedicineSeq("1")
                .firstMedicineName("hey")
                .secondMedicineSeq("2")
                .secondMedicineName("what")
                .prohibitedContent("no")
                .build();
        combinationRepository.save(avoidCombination);
        AvoidCombination newCombination = AvoidCombination.builder()
                .firstMedicineSeq("1")
                .firstMedicineName("hey")
                .secondMedicineSeq("2")
                .secondMedicineName("what")
                .prohibitedContent("no")
                .build();
        assertTrue(combinationRepository.existsByFirstMedicineSeqAndSecondMedicineSeq(newCombination.getFirstMedicineSeq(), newCombination.getSecondMedicineSeq()));
    }
}
