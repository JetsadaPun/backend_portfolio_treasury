package com.example.demo.service;

import com.example.demo.model.Subject;
import com.example.demo.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject addSubject(Subject subject) {
        if (subjectRepository.existsBySubjectNameId(subject.getSubjectNameId())) {
            throw new IllegalArgumentException("Subject code already exists");
        }
        return subjectRepository.save(subject);
    }

    public java.util.List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public org.springframework.data.domain.Page<Subject> getAllSubjects(
            org.springframework.data.domain.Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    public org.springframework.data.domain.Page<Subject> searchSubjects(String keyword,
            org.springframework.data.domain.Pageable pageable) {
        return subjectRepository.searchSubjects(keyword, pageable);
    }

    @Transactional
    public void importCSV(java.io.InputStream inputStream) {
        try (java.io.BufferedReader fileReader = new java.io.BufferedReader(
                new java.io.InputStreamReader(inputStream, java.nio.charset.StandardCharsets.UTF_8));
                com.opencsv.CSVReader csvReader = new com.opencsv.CSVReaderBuilder(fileReader)
                        .withCSVParser(new com.opencsv.RFC4180ParserBuilder().build())
                        .build()) {

            String[] nextRecord;
            boolean isHeader = true;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                // CSV Order: subjectname_id, subject_name_th, subject_name_en,
                // subject_description
                if (nextRecord.length < 3)
                    continue;

                String code = nextRecord[0].trim();
                String nameTh = nextRecord[1].trim();
                String nameEn = nextRecord[2].trim();
                String desc = nextRecord.length > 3 ? nextRecord[3].trim() : "";

                if (!subjectRepository.existsBySubjectNameId(code)) {
                    Subject s = new Subject();
                    s.setSubjectNameId(code);
                    s.setSubjectNameTh(nameTh);
                    s.setSubjectNameEn(nameEn);
                    s.setDescription(desc);
                    subjectRepository.save(s);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }

    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new IllegalArgumentException("Subject not found");
        }
        subjectRepository.deleteById(id);
    }

    public java.util.Optional<Subject> getSubjectByCode(String code) {
        return subjectRepository.findBySubjectNameId(code);
    }
}
