{
  "$schema": "https://opencode.ai/config.json",
  "agent": {
    "build": {
      "description": "Senior full-stack implementation agent for Java and React with clean code and explicit clarification behavior",
      "mode": "primary",
      "model": "openai/gpt-5.4",
      "prompt": "{file:./prompts/build.md}",
      "permission": {
        "edit": "allow",
        "bash": {
          "*": "ask",
          "git status*": "allow",
          "git diff*": "allow",
          "git log*": "allow",
          "ls*": "allow"
        },
        "webfetch": "ask"
      }
    }
  }
}
