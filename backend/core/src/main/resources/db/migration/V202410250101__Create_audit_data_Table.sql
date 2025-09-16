-- Create table audit_data

DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'audit_data') THEN
        RAISE NOTICE 'The audit_data table already exists !';
    ELSE
        CREATE TABLE audit_data (
            creation_date_time timestamp NOT NULL,
            created_by varchar(255) NOT NULL,
            last_update_date_time timestamp NOT NULL,
            updated_by varchar(255) NOT NULL
        );

        RAISE NOTICE 'audit_data table created successfully.';
    END IF;
END $$;
