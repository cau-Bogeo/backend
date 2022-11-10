package com.caubogeo.bogeo.service.medicine;

import com.caubogeo.bogeo.domain.medicine.AvoidCombination;
import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
import com.caubogeo.bogeo.domain.member.Member;
import com.caubogeo.bogeo.dto.medicine.MedicineCombinationDto;
import com.caubogeo.bogeo.dto.medicine.MedicineDetailResponseDto;
import com.caubogeo.bogeo.dto.medicine.MedicineResponseDto;
import com.caubogeo.bogeo.dto.member.UserMedicineQuery;
import com.caubogeo.bogeo.exceptionhandler.MemberException;
import com.caubogeo.bogeo.exceptionhandler.MemberExceptionType;
import com.caubogeo.bogeo.repository.CombinationRepository;
import com.caubogeo.bogeo.repository.MedicineDetailRepository;
import com.caubogeo.bogeo.repository.MemberRepository;
import com.caubogeo.bogeo.repository.UserMedicineRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineSearchService {
    private final MedicineDetailRepository medicineDetailRepository;
    private final CombinationRepository combinationRepository;
    private final UserMedicineRepository userMedicineRepository;
    private final MemberRepository memberRepository;

    public List<MedicineResponseDto> searchMedicineName(String name) {
        List<MedicineResponseDto> medicineDetailList = new ArrayList<>();
        medicineDetailRepository.findByItemNameContains(name).forEach(medicineQuery -> medicineDetailList.add(MedicineResponseDto.of(medicineQuery)));
        return medicineDetailList;
    }

    public MedicineDetailResponseDto searchMedicineDetail(String id, String medicineSeq) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_USER));
        List<UserMedicineQuery> userMedicineList = userMedicineRepository.findByUser(member);
        MedicineDetail medicineDetail = medicineDetailRepository.findByItemSeq(medicineSeq);
        List<MedicineCombinationDto> avoidCombinations = new ArrayList<>();

        if(userMedicineList.isEmpty()) {
            return MedicineDetailResponseDto.of(medicineDetail, avoidCombinations);
        }

        for(UserMedicineQuery secondMedicineSeq: userMedicineList) {
            AvoidCombination combination = combinationRepository.findByFirstMedicineSeqAndSecondMedicineSeq(medicineSeq,
                    secondMedicineSeq.getMedicineSeq());
            if(combination != null) {
                MedicineCombinationDto combinationDto = new MedicineCombinationDto(combination.getSecondMedicineName(),
                        combination.getProhibitedContent());
                avoidCombinations.add(combinationDto);
            }
        }
        return MedicineDetailResponseDto.of(medicineDetail, avoidCombinations);
    }

    public void updateMedicineDetail(String itemSeq) {
        MedicineDetail medicineDetail = medicineDetailRepository.findByItemSeq(itemSeq);
        medicineDetail.setMedicineWarning("1. 다음 환자에게는 투여하지 말것\\n이 약 성분에 과민증이 있는 환자\\n2. 다음 환자에게는 신중히 투여할 것\\n1) 신기능장애가 있는 환자\\n2) 간기능장애가 있는 환자\\n3) 고령인 환자\\n3. 이상반응\\n임상시험에서 총 증례 1,446명 중, 부작용이 137명(9.5%) 보고되었다\\n1) 정신신경계 : 졸음, 권태감, 두통, 두중감, 현기증\\n2) 소화기계 : 구갈, 구내건조, 설염, 오심, 구토, 위통, 위부불쾌감, 설사\\n3) 혈액계 : 백혈구수 변동, 호산구과다증\\n4) 간장 : AST(GOT), ALT(GPT), r-GTP, LDH, 총빌리루빈 상승\\n5) 신장 : 뇨단백, 뇨당, 뇨잠혈, 뇨우로빌리노겐\\n6) 피부 : 발진, 종창의 과민반응\\n국내 시판 후 조사 결과(정제)\\n국내에서 재심사를 위하여 6년 동안 3,717명의 환자를 대상으로 실시한 시판 후 조사 결과 유해사례 발현율은 인과관계와 상관없이 1.8%(68명3,717명, 80건)로 보고되었다. 졸음 1.26%(47명3,717명, 47건), 갈증 0.30%(11명3,717명, 11건), 복부불쾌 0.27%(10명3,717명, 10건), 권태 0.11%(4명3,717명, 4건), 얼굴부종 0.05%(2명3,717명, 2건), SGOT 상승, SGPT 상승, 두통, 두근거림, 얼굴홍조, 쓴맛 각 0.03%(1명3,717명, 1건)순으로 조사되었다.\\n\\n이 약과 인과관계를 배제할 수 없는 유해사례인 약물유해반응 발현율은 1.4%(52명3,717명, 62건)로 보고되었다. 졸음 1.13%(42명3,717명, 42건), 갈증 0.24%(9명3,717명, 9건), 복부불쾌 0.11%(4명3,717명, 4건), 권태 0.08%(3명3,717명, 3건), 얼굴홍조, SGOT 상승, SGPT 상승 각각 0.03%(1명3,717명, 1건)이었다.\\n국내 시판 후 조사기간 동안 발생한 예상하지 못한 유해사례의 발현율은 인과관계와 상관없이 0.13%(5명3,717명, 5건)이었으며, 얼굴부종 0.05%(2명3,717명, 2건), 두근거림, 얼굴홍조, 쓴맛 각각 0.03%(1명3,717명, 1건)로 보고되었다. 이 중 이 약과 인과관계를 배제할 수 없는 유해사례인 약물유해반응으로 얼굴홍조 1건이 보고되었다.\\n국내 시판 후 조사기간 동안 발생한 중대한 유해사례는 보고되지 않았다.\\n4. 일반적 주의\\n1) 졸음을 유발할 수 있으므로, 본제 투여중의 환자에게는 자동차 운전 등 위험을 수반하는 기계조작등에 대해 주의시켜야 한다.\\n2) 장기간스테로이드요법을 받고 있던 환자로, 본제를 투여함에 따라 스테로이드제를 감량할 경우에는 충분한 관리하에서 서서히 한다.\\n3) 신장애환자에서 이약의 혈중농도를 상승시킬 수 있다. 또한 높은 혈중농도가 지속될 우려가 있으므로 저용량(즉, 1회량 5밀리그람)부터 투여하는 등, 신중히 투여하고, 이상이 관찰되는 경우에는 감량, 휴약등의 적절한 처치가 필요하다.\\n4) 본제는 계절성 알러지성 비염환자에 대하여 연구되지 않은바 주의하여 사용하여야 하며, 투여하는 경우에는 호발하는 계절을 고려하여 그 직전부터 투여를 개시하고, 호발하는 계절 종료시까지 계속하는 것이 바람직하다.\\n5) 본제의 사용에 의해 효과가 확인되지 않는 경우에는 장기간 투여하지 않는다.\\n5.상호작용\\n다른 항히스타민제와 마찬가지로 과량의 알코올과 병용투여하지 않는다.\\n6. 임부, 산부, 수유부에 대한 투여\\n1) 임신중 투여에 관한 안전성은 확립되어 있지 않으며, 또한 동물실험에서 태아로 이행됨이 발견되고 있으므로, 임부 또는 임신 가능성이 있는 부인에게는 투여하지 않는 것이 바람직하지만, 투여하지 않을 수 없는 경우에는 치료상의 유익성이 위험성을 상회한다고 판단되는 경우에만 투여한다.\\n2) 동물실험(랫트)에서 모유 중으로 이행된다는 보고가 있으므로, 수유중인 부인에게는 투여하지 않는 것이 바람직하지만, 부득이하게 본제를 투여할 경우에는 수유를 피하도록 한다.\\n7.소아에 대한 투여\\n소아에 대한 안전성은 확립되어 있지 않다.\\n8. 고령자에 대한 투여\\n본제는 주로 신장으로 배설되며, 일반적으로 고령자는 생리기능이 저하되어 있으므로 높은 혈중농도가 지속될 위험이 있어 본제를 고령자에게 사용할 경우 주의하여 투여한다.\\n9. 임상검사치에의 영향\\n이 약은 알레르겐 피내반응을 억제하여 알레르겐 확인에 지장을 줄 수 있으므로 피내반응 검사 실시전에 이 약을 투약하지 않는 것이 바람직하다.\\n10. 보관 및 취급상의 주의사항\\n1) 소아의 손이 닿지 않는 곳에 보관한다.\\n2) 습기를 피하여 30℃이하에서 보관한다.\\n11. 의약품동등성시험 정보\\n가. 시험약 베포타스틴베실산염정10밀리그램(우리들제약(주))과 대조약 타리온정10밀리그램(동아에스티(주))을 2X2 교차시험으로 각 1정씩 건강한 성인에게 공복 시 단회 경구 투여하여 28명의 혈중 베포타스틴을 측정한 결과, 비교평가항목치(AUC, Cmax sub)를 로그변환하여 통계처리하였을 때, 평균치 차의 90%신뢰구간이 log 0.8에서 log 1.25 이내로서 생물학적으로 동등함을 입증하였다.");
        medicineDetailRepository.save(medicineDetail);
    }
}
