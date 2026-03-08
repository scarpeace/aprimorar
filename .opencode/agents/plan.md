{
  "$schema": "https://opencode.ai/config.json",
  "agent": {
    "plan": {
      "mode": "primary",
      "model": "openai/gpt-5.4",
      "temperature": 0.2,
      "steps": 20,
      "prompt": "{file:./.opencode/prompts/build.md}",
      "tools": {
        "write": true,
        "edit": true,
        "bash": true
      },
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
