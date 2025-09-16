-- Add pdf_url column to table legal_process

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'legal_process' AND column_name = 'pdf_url') THEN
        RAISE NOTICE 'The pdf_url column already exists in legal_process table !';
    ELSE
        ALTER TABLE legal_process ADD COLUMN pdf_url VARCHAR(255);
        RAISE NOTICE 'The pdf_url column has been added to legal_process table !';
    END IF;
END $$;
