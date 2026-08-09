INSERT INTO skills (id, name, normalized_name) VALUES
 ('00000000-0000-0000-0000-000000000001', 'Java', 'java'),
 ('00000000-0000-0000-0000-000000000002', 'Spring Boot', 'spring boot'),
 ('00000000-0000-0000-0000-000000000003', 'Python', 'python'),
 ('00000000-0000-0000-0000-000000000004', 'AI/ML', 'ai/ml'),
 ('00000000-0000-0000-0000-000000000005', 'React', 'react'),
 ('00000000-0000-0000-0000-000000000006', 'UI/UX', 'ui/ux'),
 ('00000000-0000-0000-0000-000000000007', 'Cloud', 'cloud'),
 ('00000000-0000-0000-0000-000000000008', 'PostgreSQL', 'postgresql'),
 ('00000000-0000-0000-0000-000000000009', 'REST APIs', 'rest apis'),
 ('00000000-0000-0000-0000-000000000010', 'Git', 'git')
ON CONFLICT (normalized_name) DO NOTHING;

INSERT INTO hackathons (id, name, description, organizer, start_date, end_date, registration_deadline, mode, location, team_size_min, team_size_max, status, registration_url)
VALUES ('10000000-0000-0000-0000-000000000001', 'Google GenAI Hackathon', 'Build useful products with generative AI and responsible technology.', 'Google', CURRENT_TIMESTAMP + INTERVAL '14 days', CURRENT_TIMESTAMP + INTERVAL '16 days', CURRENT_TIMESTAMP + INTERVAL '7 days', 'ONLINE', 'Online', 2, 5, 'OPEN', 'https://developers.google.com/events')
ON CONFLICT DO NOTHING;

INSERT INTO hackathon_skills (hackathon_id, skill_id, importance, required)
VALUES
 ('10000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000004', 5, TRUE),
 ('10000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', 4, TRUE),
 ('10000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000002', 4, TRUE),
 ('10000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000007', 3, FALSE),
 ('10000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000005', 3, FALSE)
ON CONFLICT DO NOTHING;

INSERT INTO hackathon_tracks (hackathon_id, name, description)
VALUES ('10000000-0000-0000-0000-000000000001', 'AI for Education', 'Create tools that make learning more personal and accessible.')
ON CONFLICT DO NOTHING;

INSERT INTO internships (id, company, title, description, location, work_mode, application_deadline, application_url, status)
VALUES ('20000000-0000-0000-0000-000000000001', 'Microsoft', 'Software Engineering Intern', 'Build reliable services and delightful developer experiences with a modern engineering team.', 'Bengaluru', 'HYBRID', CURRENT_TIMESTAMP + INTERVAL '45 days', 'https://careers.microsoft.com/', 'OPEN')
ON CONFLICT DO NOTHING;

INSERT INTO internship_skills (internship_id, skill_id, required)
VALUES
 ('20000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', TRUE),
 ('20000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000002', TRUE),
 ('20000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000009', TRUE),
 ('20000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000010', FALSE),
 ('20000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000007', FALSE)
ON CONFLICT DO NOTHING;
