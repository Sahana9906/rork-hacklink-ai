package com.rork.hacklinkai.data

import com.rork.hacklinkai.model.Achievement
import com.rork.hacklinkai.model.Hackathon
import com.rork.hacklinkai.model.Internship
import com.rork.hacklinkai.model.Notification
import com.rork.hacklinkai.model.NotificationType
import com.rork.hacklinkai.model.Project
import com.rork.hacklinkai.model.Skill
import com.rork.hacklinkai.model.TeammateMatch
import com.rork.hacklinkai.model.User

object MockData {
    val user: User = User(
        name = "Sahana B",
        role = "Backend Developer",
        experienceLevel = "Intermediate",
        interests = listOf("AI / ML", "Developer Tools", "Open Source"),
        availability = "Evenings & weekends",
        profileStrength = 92,
        skills = listOf(
            Skill("Java", 94, "Detected from 4 GitHub repositories"),
            Skill("Spring Boot", 88, "Detected from 2 backend projects"),
            Skill("Python", 81, "Detected from 3 project descriptions"),
            Skill("React", 73, "Detected from 2 frontend projects"),
            Skill("AI / ML", 82, "Detected from AI project descriptions"),
            Skill("PostgreSQL", 85, "Detected from 5 repositories")
        ),
        projects = listOf(
            Project("Campus Copilot", "An AI assistant for student services.", listOf("Java", "Spring Boot", "OpenAI")),
            Project("Pulse Boards", "Realtime collaboration for hackathon teams.", listOf("React", "WebSockets", "PostgreSQL")),
            Project("SpendWise", "Personal finance insights for students.", listOf("Python", "FastAPI", "ML"))
        ),
        hackathonsJoined = 6,
        connections = 128,
        githubConnected = true
    )

    val hackathons: List<Hackathon> = listOf(
        Hackathon(
            id = "google-genai",
            name = "Google GenAI Hackathon",
            match = 96,
            category = "AI • GenAI • Cloud",
            mode = "Online",
            duration = "36 hours",
            date = "18–20 Oct 2026",
            teamSize = "2–4 members",
            requiredSkills = listOf("AI / ML", "React", "Python", "Cloud", "Backend"),
            tracks = listOf("Generative AI", "AI Agents", "LLM Applications"),
            matchingReasons = listOf("Strong AI experience", "Backend development experience", "React experience", "Relevant projects"),
            skillGap = "Cloud deployment",
            accentHex = 0xFF5B4CF6
        ),
        Hackathon(
            id = "ai-innovation",
            name = "AI Innovation Challenge",
            match = 91,
            category = "AI • Social Impact",
            mode = "Hybrid",
            duration = "48 hours",
            date = "02–04 Nov 2026",
            teamSize = "2–5 members",
            requiredSkills = listOf("Python", "AI / ML", "Product"),
            tracks = listOf("Responsible AI", "Accessibility", "Civic Tech"),
            matchingReasons = listOf("AI project experience", "Strong backend foundation", "Impact-oriented interests"),
            skillGap = "Model deployment",
            accentHex = 0xFF2563EB
        ),
        Hackathon(
            id = "smart-india",
            name = "Smart India Hackathon",
            match = 87,
            category = "Civic Tech • Cloud",
            mode = "In-person",
            duration = "36 hours",
            date = "14–16 Nov 2026",
            teamSize = "6 members",
            requiredSkills = listOf("Java", "Cloud", "Data", "Research"),
            tracks = listOf("Digital India", "Sustainability", "Healthcare"),
            matchingReasons = listOf("Full-stack project history", "Team experience", "Problem-solving strength"),
            skillGap = "Cloud architecture",
            accentHex = 0xFF119A69
        ),
        Hackathon(
            id = "android-innovation",
            name = "Android Innovation Hackathon",
            match = 84,
            category = "Android • AI",
            mode = "Online",
            duration = "24 hours",
            date = "28–29 Nov 2026",
            teamSize = "2–4 members",
            requiredSkills = listOf("Kotlin", "Android", "AI"),
            tracks = listOf("On-device AI", "Community", "Productivity"),
            matchingReasons = listOf("AI fluency", "Product thinking", "Fast prototyping"),
            skillGap = "Kotlin fundamentals",
            accentHex = 0xFFF59E0B
        )
    )

    val teammates: List<TeammateMatch> = listOf(
        TeammateMatch("ananya", "Ananya Nair", "Frontend Developer", listOf("React", "TypeScript"), 97, 0xFF7C5CFC, "Your frontend skills complement the user's backend and AI experience.", "Weeknights"),
        TeammateMatch("karthik", "Karthik Reddy", "UI/UX Designer", listOf("Figma", "UI Design"), 95, 0xFF0EA5A8, "Strong product taste fills the team's design gap.", "Flexible"),
        TeammateMatch("rahul", "Rahul Sharma", "ML Engineer", listOf("Python", "Machine Learning"), 93, 0xFFE8793A, "ML depth strengthens the team's model-building track.", "Weekends")
    )

    val internships: List<Internship> = listOf(
        Internship("microsoft", "Microsoft", "Software Engineering Intern", 92, "Bengaluru / Remote", "Hybrid", "12 weeks", listOf("Java", "Spring Boot", "Git", "REST APIs"), "Cloud", 0xFF2563EB),
        Internship("google", "Google", "Student Researcher — AI", 95, "Bengaluru", "Hybrid", "10 weeks", listOf("Python", "Machine Learning", "Git", "Research"), "Model evaluation", 0xFF119A69),
        Internship("zoho", "Zoho", "Backend Engineering Intern", 88, "Chennai", "On-site", "12 weeks", listOf("Java", "REST APIs", "SQL"), "Distributed systems", 0xFFF59E0B),
        Internship("atlassian", "Atlassian", "Frontend Platform Intern", 91, "Remote — India", "Remote", "16 weeks", listOf("React", "TypeScript", "Git"), "Design systems", 0xFF7C5CFC)
    )

    val notifications: List<Notification> = listOf(
        Notification("deadline", "Submission deadline is tomorrow", "Google GenAI Hackathon closes at 11:59 PM.", "12 min ago", NotificationType.DEADLINE),
        Notification("accepted", "Ananya accepted your team invitation", "Your team is now 2 members strong.", "2 hours ago", NotificationType.TEAM),
        Notification("match", "New teammate match found", "Priya matches your AI Innovation Challenge profile.", "Yesterday", NotificationType.TEAM),
        Notification("internship", "Your internship matches are refreshed", "Three roles improved after your latest project.", "2 days ago", NotificationType.OPPORTUNITY, false)
    )

    val achievements: List<Achievement> = listOf(
        Achievement("Hackathon Finalist", "Google Solution Challenge 2025"),
        Achievement("AI Builder", "3 AI projects shipped"),
        Achievement("Open Source Contributor", "12 merged pull requests")
    )
}
