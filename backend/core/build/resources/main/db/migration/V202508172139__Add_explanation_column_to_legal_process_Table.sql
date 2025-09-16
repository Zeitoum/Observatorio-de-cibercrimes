-- Add justification column to table legal_process

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'legal_process' AND column_name = 'justification') THEN
        RAISE NOTICE 'The justification column already exists in legal_process table !';
    ELSE
        ALTER TABLE legal_process ADD COLUMN justification text;
        RAISE NOTICE 'The justification column has been added to legal_process table !';
    END IF;
END $$;
