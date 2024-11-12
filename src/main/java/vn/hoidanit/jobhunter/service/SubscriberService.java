package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public boolean isExistByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber create(Subscriber req) {
        Subscriber newSubscriber = new Subscriber();
        newSubscriber.setName(req.getName());
        newSubscriber.setEmail(req.getEmail());
        List<Skill> skills = req.getSkills().stream()
                .map(item -> this.skillRepository.findById(item.getId()).orElse(null))
                .toList();
        newSubscriber.setSkills(skills);

        return this.subscriberRepository.save(newSubscriber);
    }

    public Subscriber update(Subscriber subsDB, Subscriber req) {
        if (req.getSkills() != null) {
            List<Long> reqSkills = req.getSkills()
                    .stream().map(Skill::getId)
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subsDB.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subsDB);

    }

    public Subscriber findById(long id) {
        Optional<Subscriber> subsOptional = this.subscriberRepository.findById(id);
        return subsOptional.orElse(null);
    }

}
