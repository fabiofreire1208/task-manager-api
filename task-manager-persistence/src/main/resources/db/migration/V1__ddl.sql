-- Create the table for Users
CREATE TABLE IF NOT EXISTS users (
    id UUID NOT NULL PRIMARY KEY,          -- Auto-incrementing ID
    name VARCHAR(100) NOT NULL,       -- Name of the user
    email VARCHAR(150) UNIQUE NOT NULL -- Email of the user, must be unique
);

-- Create the table for Projects
CREATE TABLE IF NOT EXISTS projects (
    id UUID NOT NULL PRIMARY KEY,            -- Auto-incrementing ID
    name VARCHAR(150) NOT NULL, -- Name of the project
    description VARCHAR(250)                  -- Description of the project
);

-- Create the table for Tasks
CREATE TABLE IF NOT EXISTS tasks (
    id UUID NOT NULL PRIMARY KEY,            -- Auto-incrementing ID
    title VARCHAR(150) NOT NULL,  -- Name of the task
    description VARCHAR(250),
    status VARCHAR(20) NOT NULL CHECK (status IN ('OPENED', 'IN_PROGRESS', 'COMPLETED')), -- Status with specific allowed values
    priority VARCHAR(10) NOT NULL CHECK (priority IN ('LOW', 'NORMAL', 'HIGH')), -- Priority with specific allowed values
    deadline TIMESTAMP NOT NULL,      -- Deadline for the task
    user_id UUID,                      -- Foreign key referencing the assigned user
    project_id UUID NOT NULL,          -- Foreign key referencing the project
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE SET NULL,           -- Set to NULL if the user is deleted
    CONSTRAINT fk_project
        FOREIGN KEY (project_id)
        REFERENCES projects (id)
        ON DELETE CASCADE             -- Delete tasks if the associated project is deleted
);

-- Create the table for Devices
CREATE TABLE IF NOT EXISTS devices (
    id UUID NOT NULL PRIMARY KEY,            -- Auto-incrementing ID
    device_name VARCHAR(100) NOT NULL, -- Name of the device
    user_id UUID NOT NULL,             -- Foreign key referencing the user
    CONSTRAINT fk_user_device
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE             -- Delete devices if the associated user is deleted
);

CREATE TABLE IF NOT EXISTS task_watchers (
    task_id UUID NOT NULL,
    user_id UUID NOT NULL,
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES tasks(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Index for faster lookup on user email in the users table
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users (email);

-- Index for faster lookup on task name in the tasks table
CREATE INDEX IF NOT EXISTS idx_tasks_title ON tasks (title);

-- Index for faster lookup on project name in the projects table
CREATE INDEX IF NOT EXISTS idx_projects_project_name ON projects (name);