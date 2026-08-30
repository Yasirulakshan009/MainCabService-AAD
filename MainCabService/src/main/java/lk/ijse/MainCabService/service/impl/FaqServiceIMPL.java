package lk.ijse.MainCabService.service.impl;

import lk.ijse.MainCabService.dto.FaqDTO;
import lk.ijse.MainCabService.entity.Faq;
import lk.ijse.MainCabService.repository.FaqRepository;
import lk.ijse.MainCabService.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FaqServiceIMPL implements FaqService {

    private final FaqRepository faqRepository;

    @Override
    public List<FaqDTO> getAllFAQs() {
        log.info("Executing getAllFAQs()");
        try {
            List<Faq> faqList = faqRepository.findAll();
            List<FaqDTO> dtoList = new ArrayList<>();

            for (Faq faq : faqList) {
                FaqDTO dto = new FaqDTO();
                dto.setId(faq.getId());
                dto.setQuestion(faq.getQuestion());
                dto.setAnswer(faq.getAnswer());

                dtoList.add(dto);
            }

            log.info("Fetched " + dtoList.size() + " FAQs successfully.");
            return dtoList;

        } catch (Exception e) {
            log.error("Error in getAllFAQs(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public FaqDTO saveFAQ(FaqDTO faqDTO) {
        log.info("Executing saveFAQ()");
        try {
            Faq faq = new Faq();
            faq.setQuestion(faqDTO.getQuestion().trim());
            faq.setAnswer(faqDTO.getAnswer().trim());

            Faq saved = faqRepository.save(faq);

            FaqDTO dto = new FaqDTO();
            dto.setId(saved.getId());
            dto.setQuestion(saved.getQuestion());
            dto.setAnswer(saved.getAnswer());

            log.info("FAQ saved successfully!");
            return dto;
        } catch (Exception e) {
            log.error("Error in saveFAQ(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public FaqDTO updateFAQ(Long id, FaqDTO faqDTO) {
        log.info("Executing updateFAQ() for ID: " + id);
        try {
            Optional<Faq> optionalFAQ = faqRepository.findById(id);
            if (!optionalFAQ.isPresent()) {
                throw new RuntimeException("FAQ not found with ID: " + id);
            }

            Faq faq = optionalFAQ.get();
            faq.setQuestion(faqDTO.getQuestion().trim());
            faq.setAnswer(faqDTO.getAnswer().trim());

            Faq updated = faqRepository.save(faq);

            FaqDTO dto = new FaqDTO();
            dto.setId(updated.getId());
            dto.setQuestion(updated.getQuestion());
            dto.setAnswer(updated.getAnswer());

            log.info("FAQ updated successfully!");
            return dto;

        } catch (Exception e) {
            log.error("Error in updateFAQ(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFAQ(Long id) {

        log.info("Executing deleteFAQ() for ID: " + id);
        try {
            Optional<Faq> optionalFAQ = faqRepository.findById(id);
            if (!optionalFAQ.isPresent()) {
                throw new RuntimeException("FAQ not found with ID: " + id);
            }

            faqRepository.deleteById(id);
            log.info("FAQ deleted successfully for ID: " + id);
        } catch (Exception e) {
            log.error("Error in deleteFAQ(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
